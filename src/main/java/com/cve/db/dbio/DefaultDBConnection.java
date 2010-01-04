
package com.cve.db.dbio;

import com.cve.db.dbio.driver.DefaultDBMetaData;
import com.cve.db.dbio.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.db.ConnectionInfo;
import com.cve.db.SQL;
import com.cve.db.Server;
import com.cve.log.Log;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ServersStore;
import com.cve.stores.UnpredictableFunction;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import static com.cve.log.Log.args;
import static com.cve.util.Check.notNull;

/**
 * A database connection.
 * Connection reuse is essential for speed, since connections take so long
 * to establish.  Pooling, however probably isn't necessary.
 * <p>
 * Since connections can timeout, we use a factory that knows how to create them,
 * and then recreate them as needed.
 */
final class DefaultDBConnection implements DBConnection {


    /**
     * How we talk to the database.
     * We don't expose this, because we may need to reset it.
     */
    private volatile Connection connection;

    /**
     * How we generate connections
     */
    public final ConnectionInfo info;

    /**
     * For getting info about the database.
     */
    public final DBMetaData dbMetaData;

    final ServersStore serversStore;

    private final ManagedFunction<SQL,DBResultSetIO> resultSets;

    private static final Log LOG = Log.of(DBConnection.class);

    private DefaultDBConnection(ConnectionInfo info, ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.info = notNull(info);
        this.serversStore = serversStore;
        dbMetaData = DefaultDBMetaData.getDbmd(this,managedFunction,serversStore);
        resultSets = managedFunction.of(new ExecuteSQL());
    }

    public static DBConnection info(ConnectionInfo info, ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new DefaultDBConnection(info,serversStore,managedFunction);
    }

    synchronized private Connection getConnection() throws SQLException {
        if (connection==null) {
            return reset();
        }
        return connection;
    }

    @Override
    public synchronized DBResultSetMetaData getMetaData(Server server, DBResultSetIO results) throws SQLException {
        args(server,results);
        return DefaultDBResultSetMetaDataFactory.of(server,this,results);
    }

    synchronized Connection reset() throws SQLException {
        info("resetting " + info);
        connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        return connection;
    }

    public synchronized DatabaseMetaData getJDBCMetaData() {
        try {
            return getJDBCMetaData0();
        } catch (SQLException e) {
            warn(e);
            try {
                reset();
                return getJDBCMetaData0();
            } catch (SQLException e2) {
                throw new RuntimeException(e2);
            }
        }
    }


    private DatabaseMetaData getJDBCMetaData0() throws SQLException {
        return DatabaseMetaDataWrapper.of(getConnection().getMetaData());
    }

    /**
     * Execute the given SQL.
     */
    @Override
    public synchronized CurrentValue<DBResultSetIO> select(final SQL sql) throws SQLException {
        return resultSets.apply(sql);
    }

    @Override
    public ConnectionInfo getInfo() {
        return info;
    }

    @Override
    public DBMetaData getMetaData() {
        return dbMetaData;
    }

    DBMetaData getDbmd(Server server) {
        DefaultDBConnection connection = (DefaultDBConnection) serversStore.getConnection(server);
        DBMetaData   dbmd = connection.dbMetaData;
        return dbmd;
    }
    
    static void info(String message) {
        LOG.info(message);
    }

    static void warn(Throwable t) {
        LOG.warn(t);
    }

    private final class ExecuteSQL implements UnpredictableFunction<SQL,DBResultSetIO> {

        @Override
        public DBResultSetIO apply(SQL sql) throws Exception {
            Statement statement = getConnection().createStatement();
            String sqlString = sql.toString();
            info(sqlString);
            statement.execute(sqlString);
            return DBResultSetIO.of(ResultSetWrapper.of(statement.getResultSet()));
        }

   }


}

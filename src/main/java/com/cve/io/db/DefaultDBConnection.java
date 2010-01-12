
package com.cve.io.db;

import com.cve.io.db.driver.DefaultDBMetaData;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.SQL;
import com.cve.model.db.DBServer;
import com.cve.log.Log;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
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
    public final DBConnectionInfo info;

    /**
     * For getting info about the database.
     */
    public final DBMetaData dbMetaData;

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private final ManagedFunction<SQL,DBResultSetIO> resultSets;

    private static final Log LOG = Log.of(DBConnection.class);

    private DefaultDBConnection(
        DBConnectionInfo info, DBServersStore serversStore, ManagedFunction.Factory managedFunction)
    {
        this.info = notNull(info);
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
        dbMetaData = DefaultDBMetaData.getDbmd(this,managedFunction,serversStore);
        resultSets = managedFunction.of(new ExecuteSQL());
    }

    static DefaultDBConnection of(DBConnectionInfo info, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new DefaultDBConnection(info,serversStore,managedFunction);
    }

    synchronized private Connection getConnection() throws SQLException {
        if (connection==null) {
            return reset();
        }
        return connection;
    }

    @Override
    public synchronized DBResultSetMetaData getMetaData(DBServer server, DBResultSetIO results)  {
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
    public synchronized CurrentValue<DBResultSetIO> select(final SQL sql) {
        return resultSets.apply(sql);
    }

    @Override
    public DBConnectionInfo getInfo() {
        return info;
    }

    @Override
    public DBMetaData getMetaData() {
        return dbMetaData;
    }

    DBMetaData getDbmd(DBServer server) {
        DefaultDBConnection connection = (DefaultDBConnection) DBConnectionFactory.getConnection(info, serversStore, managedFunction);
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

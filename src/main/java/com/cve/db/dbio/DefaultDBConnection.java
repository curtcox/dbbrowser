
package com.cve.db.dbio;

import com.cve.db.dbio.driver.DefaultDBMetaData;
import com.cve.db.dbio.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.db.ConnectionInfo;
import com.cve.db.SQL;
import com.cve.db.Server;
import com.cve.log.Log;
import com.cve.stores.Stores;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
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

    private static final Log LOG = Log.of(DBConnection.class);

    private DefaultDBConnection(ConnectionInfo info) {
        this.info = notNull(info);
        dbMetaData = DefaultDBMetaData.getDbmd(this);
    }

    public static DBConnection info(ConnectionInfo info) {
        return new DefaultDBConnection(info);
    }

    synchronized private Connection getConnection() throws SQLException {
        if (connection==null) {
            return reset();
        }
        return connection;
    }

    @Override
    public synchronized DBResultSetMetaData getMetaData(Server server, ResultSet results) throws SQLException {
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
    public synchronized ResultSet select(final SQL sql) throws SQLException {
        return ResultSetRetry.run(this,new ResultSetGenerator() {
            @Override
            public ResultSet generate() throws SQLException {
                Statement statement = getConnection().createStatement();
                String sqlString = sql.toString();
                info(sqlString);
                statement.execute(sqlString);
                return ResultSetWrapper.of(statement.getResultSet());
            }
        });
    }

    @Override
    public ConnectionInfo getInfo() {
        return info;
    }

    @Override
    public DBMetaData getMetaData() {
        return dbMetaData;
    }

    static DBMetaData getDbmd(Server server) {
        DefaultDBConnection connection = (DefaultDBConnection) Stores.getServerStores().getConnection(server);
        DBMetaData   dbmd = connection.dbMetaData;
        return dbmd;
    }
    
    static void info(String message) {
        LOG.info(message);
    }

    static void warn(Throwable t) {
        LOG.warn(t);
    }


}

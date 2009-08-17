
package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.SQL;
import com.cve.db.Server;
import com.cve.stores.ServersStore;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static com.cve.util.Check.notNull;

/**
 * A database connection.
 * Connection reuse is essential for speed, since connections take so long
 * to establish.  Pooling, however probably isn't necessary.
 * <p>
 * Since connections can timeout, we use a factory that knows how to create them,
 * and then recreate them as needed.
 */
public final class DBConnection {

    private volatile Connection connection;
    public final ConnectionInfo info;

    private DBConnection(ConnectionInfo info) {
        this.info = notNull(info);
    }

    public static DBConnection info(ConnectionInfo info) {
        return new DBConnection(info);
    }

    synchronized private Connection getConnection() throws SQLException {
        if (connection==null) {
            return reset();
        }
        return connection;
    }

    synchronized Connection reset() throws SQLException {
        log("resetting " + info);
        connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        return connection;
    }


    public synchronized DBMetaData getMetaData() {
        return DefaultDBMetaData.getDbmd(this);
    }

    public synchronized DatabaseMetaData getJDBCMetaData() {
        try {
            return getJDBCMetaData0();
        } catch (SQLException e) {
            try {
                reset();
                return getJDBCMetaData0();
            } catch (SQLException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private DatabaseMetaData getJDBCMetaData0() throws SQLException {
        return getConnection().getMetaData();
    }

    /**
     * Execute the given SQL.
     */
    public synchronized ResultSet exec(final SQL sql) throws SQLException {
        return ResultSetRetry.run(this,new ResultSetGenerator() {
            public ResultSet generate() throws SQLException {
                Statement statement = getConnection().createStatement();
                statement.execute(sql.toString());
                return statement.getResultSet();
            }
        });
    }

    public static DBMetaData getDbmd(Server server) {
        DBConnection connection = ServersStore.getConnection(server);
        DBMetaData   dbmd = connection.getMetaData();
        return dbmd;
    }

    static void log(String message) {
        System.out.println(message);
    }
}


package com.cve.io.db;

import com.cve.io.db.driver.DBDrivers;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DBDriver;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.SQL;
import com.cve.model.db.DBServer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static com.cve.util.Check.notNull;

/**
 * A database connection.
 * Connection reuse is essential for speed, since connections take so long
 * to establish.  Pooling, however probably isn't necessary.
 * <p>
 * Since connections can timeout, we use a factory that knows how to create them,
 * and then recreate them as needed.
 */
public final class DefaultDBConnection implements DBConnection {


    /**
     * How we talk to the database.
     * We don't expose this, because we may need to reset it.
     */
    private volatile Connection connection;

    /**
     * How we generate connections
     */
    public final DBConnectionInfo info;

    public final DBDriver driver;

    private final DBServersStore serversStore;

    private final ManagedFunction.Factory managedFunction;

    private final ManagedFunction<SQL,DBResultSetIO> resultSets;

    private final Log log = Logs.of();

    private DefaultDBConnection(
        DBDriver driver, DBConnectionInfo info, DBServersStore serversStore, ManagedFunction.Factory managedFunction)
    {
        this.driver = notNull(driver);
        this.info = notNull(info);
        this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        resultSets = managedFunction.of(new ExecuteSQL(),SQL.class,DBResultSetIO.class,DBResultSetIO.NULL);
    }

    public static DefaultDBConnection of(
        DBConnectionInfo info, DBServersStore serversStore, ManagedFunction.Factory managedFunction)
    {
        DBDriver driver = DBDrivers.of(managedFunction, serversStore).url(info.url);
        return new DefaultDBConnection(driver,info,serversStore,managedFunction);
    }

    synchronized private Connection getConnection() throws SQLException {
        if (connection==null) {
            return reset();
        }
        return connection;
    }

    @Override
    public synchronized DBResultSetMetaData getMetaData(DBServer server, DBResultSetIO results)  {
        log.args(server,results);
        DefaultDBResultSetMetaDataFactory factory = driver.getResultSetFactory(server, null);
        return factory.of(server, this, results);
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
        CurrentValue<DBResultSetIO> value = resultSets.apply(sql);
        return value;
    }

    @Override
    public DBConnectionInfo getInfo() {
        return info;
    }

    @Override
    public DBMetaData getMetaData() {
        return driver.getDBMetaData(this);
    }

    void info(String message) {
        log.info(message);
    }

    void warn(Throwable t) {
        log.warn(t);
    }

    private final class ExecuteSQL extends SQLFunction<SQL,DBResultSetIO> {

        ExecuteSQL() {
            super(SQL.class,DBResultSetIO.class);
        }

        @Override
        public DBResultSetIO apply(SQL sql) throws Exception {
            Statement statement = getConnection().createStatement();
            String sqlString = sql.toString();
            info(sqlString);
            statement.execute(sqlString);
            ResultSet resultSet = ResultSetWrapper.of(statement.getResultSet());
            DBResultSetIO io = DBResultSetIO.of(resultSet);
            return io;
        }

   }


}

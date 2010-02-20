package com.cve.sample.db;

import com.cve.io.db.driver.DBDriver;
import com.cve.io.db.driver.DBDrivers;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SQL;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnmanagedFunctionFactory;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.util.Check;
import com.cve.util.URIs;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates and loads a sample H2 server.
 * @author Curt
 */
public final class SampleH2Server {

    private final Connection connection;

    private final Log log  = Logs.of();

    /**
     * Our sample server.
     */
    public static final DBServer SAMPLE = DBServer.uri(URIs.of("SAMPLE"));

    private static final SampleH2Server SERVER = new SampleH2Server(getConnection());

    private boolean loaded = false;

    private SampleH2Server(Connection connection) {
        this.connection = Check.notNull(connection);
    }

    public static synchronized SampleH2Server of() {
        if (!SERVER.loaded) {
            try {
                SERVER.loadServer();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
        return SERVER;
    }

    /**
     * See http://www.h2database.com/html/features.html#database_url
     */
    public static DBConnectionInfo getConnectionInfo() {
        final String url = "jdbc:h2:mem:" + SAMPLE.toString();
        final String user = "";
        final String password = "";
        final JDBCURL jdbcURL = JDBCURL.uri(URIs.of(url));
        ManagedFunction.Factory managedFunction = UnmanagedFunctionFactory.of();
        DBServersStore dbServersStore = MemoryDBServersStore.of();
        DBDriver driver = DBDrivers.of(managedFunction, dbServersStore).url(jdbcURL);
        return DBConnectionInfo.urlUserPassword(jdbcURL, user, password,driver);
    }

    /**
     * Add the server to the store of servers.
     */
    public static void addToStore(DBServersStore serversStore) {
        serversStore.put(SAMPLE, getConnectionInfo());
    }

    /**
     * Load the databases and tables
     */
    private void loadServer() throws SQLException, IOException {
        SampleGeoDB.of(this).createAndLoadTables();
        SakilaDB.of(this).createAndLoadTables();
    }

    void createSchema(Database database) throws SQLException {
        String sql = "CREATE SCHEMA " + database.name + ";";
        update(SQL.of(sql));
    }

    /**
     * For inserting rows into a table.
     */
    class Inserter {

        /**
         * The table we insert into.
         */
        final DBTable table;

        Inserter(DBTable table) {
            this.table = table;
        }

        /**
         * Add a row with these vales.
         */
        Inserter add(Object... values) throws SQLException {
            String sql = "INSERT INTO " + table.fullName() + " VALUES(" + commaSeperated(values) + ")";
            update(SQL.of(sql));
            return this;
        }
    }

    /**
     * Given a list of values, create CSV for them.
     * This is used for building insert statements.
     */
    private static String commaSeperated(Object... values) {
        StringBuilder out = new StringBuilder();
        for (int i=0; i<values.length; i++) {
            Object value = values[i];
            if (value instanceof Integer) {
                out.append("" + value);
            }
            if (value instanceof String) {
                out.append("'" + value + "'");
            }
            if (i + 1 < values.length) {
                out.append(",");
            }
        }
        return out.toString();
    }

    /**
     * Create the given table and return a way to fill it.
     */
    Inserter makeTable(DBTable table, String columns) throws SQLException {
        createTable(table,columns);
        return new Inserter(table);
    }

    /**
     * Create a new database table.
     */
    void createTable(DBTable table, String columns)
        throws SQLException
    {
        String sql = "CREATE TABLE " + table + "(" + columns + ");";
        update(SQL.of(sql));
        select(SQL.of("SELECT COUNT(*) FROM " + table));
        select(SQL.of("SELECT * FROM " + table));
    }

    void update(SQL sql) throws SQLException {
        Statement statement = connection.createStatement();
        info(sql.toString());
        boolean isResultSet = statement.execute(sql.toString());
        statement.close();
        info("Result Set? " + isResultSet);
    }

    public ResultSet select(SQL sql) throws SQLException {
        final DBConnectionInfo info = getConnectionInfo();
        Statement statement = connection.createStatement();
        info(sql.toString());
        boolean isResultSet = statement.execute(sql.toString());
        ResultSet results = statement.getResultSet();
        info("Result Set? " + isResultSet);
        return results;
    }

    static void info(String message) {
        // System.out.println(message);
    }

    private static Connection getConnection() {
        try {
            final DBConnectionInfo info = getConnectionInfo();
            Connection connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        SampleH2Server server = SampleH2Server.of();
        SQL sql = SQL.of("SELECT COUNT(*) FROM GEO.CITIES");
        ResultSet results = server.select(sql);
        results.next();
        System.out.println(results.getObject(1));
        System.out.println("Done.");
        System.exit(0);
    }

}

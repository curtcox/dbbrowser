package com.cve.sample.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SQL;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBConnection;
import com.cve.io.db.DBConnectionFactory;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates and loads a sample H2 server.
 * @author Curt
 */
public final class SampleServer {

    /**
     * Our sample server.
     */
    static final DBServer SAMPLE = DBServer.uri(URIs.of("SAMPLE"));

    /**
     * How we connect to it.
     */
    private final DBConnection connection;

    private SampleServer(DBConnection connection) {
        this.connection = connection;
        loadServer();
    }

    public static SampleServer of(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new SampleServer(getConnection(serversStore,managedFunction));
    }

    private static DBConnection getConnection(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        final DBConnectionInfo info = getConnectionInfo();
        return DBConnectionFactory.getConnection(info,serversStore,managedFunction);
    }

    /**
     * See http://www.h2database.com/html/features.html#database_url
     */
    static DBConnectionInfo getConnectionInfo() {
        final String url = "jdbc:h2:mem:" + SAMPLE.toString();
        final String user = "";
        final String password = "";
        final JDBCURL jdbcURL = JDBCURL.uri(URIs.of(url));
        return DBConnectionInfo.urlUserPassword(jdbcURL, user, password);
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
    void loadServer() {
        SampleDB.of(this).createAndLoadTables();
        SakilaDB.of(getConnectionInfo()).loadDatabase();
    }

    static void createSchema(Database database) throws SQLException {
        String sql = "CREATE SCHEMA " + database.name + ";";
        update(SQL.of(sql));
    }

    /**
     * For inserting rows into a table.
     */
    static class Inserter {

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
    static String commaSeperated(Object... values) {
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
        connection.select(SQL.of("SELECT COUNT(*) FROM " + table));
        connection.select(SQL.of("SELECT * FROM " + table));
    }

    static void update(SQL sql) throws SQLException {
        final DBConnectionInfo info = getConnectionInfo();
        Connection connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
    }
}

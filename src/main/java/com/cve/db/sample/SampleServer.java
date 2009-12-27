package com.cve.db.sample;

import com.cve.db.ConnectionInfo;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.JDBCURL;
import com.cve.db.SQL;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBConnectionFactory;
import com.cve.stores.ServersStore;
import com.cve.stores.Stores;
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
    static final Server SAMPLE = Server.uri(URIs.of("SAMPLE"));

    /**
     * How we connect to it.
     */
    private static final DBConnection connection = getConnection();

    /**
     * The static initializer does all the work -- once.
     */
    public static void load() {}

    static {
        addToStore();
        loadServer();
    }

    static DBConnection getConnection() {
        final ConnectionInfo info = getConnectionInfo();
        return DBConnectionFactory.of(info);
    }

    /**
     * See http://www.h2database.com/html/features.html#database_url
     */
    static ConnectionInfo getConnectionInfo() {
        final String url = "jdbc:h2:mem:" + SAMPLE.toString();
        final String user = "";
        final String password = "";
        final JDBCURL jdbcURL = JDBCURL.uri(URIs.of(url));
        return ConnectionInfo.urlUserPassword(jdbcURL, user, password);
    }

    /**
     * Add the server to the store of servers.
     */
    static void addToStore() {
        Stores.getServerStores().addServer(SAMPLE, getConnectionInfo());
    }

    /**
     * Load the databases and tables
     */
    static void loadServer() {
        SampleDB.load();
        SakilaDB.load();
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
    static Inserter makeTable(DBTable table, String columns) throws SQLException {
        createTable(table,columns);
        return new Inserter(table);
    }

    /**
     * Create a new database table.
     */
    static void createTable(DBTable table, String columns)
        throws SQLException
    {
        String sql = "CREATE TABLE " + table + "(" + columns + ");";
        update(SQL.of(sql));
        connection.select(SQL.of("SELECT COUNT(*) FROM " + table)).close();
        connection.select(SQL.of("SELECT * FROM " + table)).close();
    }

    static void update(SQL sql) throws SQLException {
        final ConnectionInfo info = getConnectionInfo();
        Connection connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
    }
}

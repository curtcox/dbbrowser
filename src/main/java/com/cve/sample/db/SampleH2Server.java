package com.cve.sample.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SQL;
import com.cve.model.db.DBServer;
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
public final class SampleH2Server {

    /**
     * Our sample server.
     */
    public static final DBServer SAMPLE = DBServer.uri(URIs.of("SAMPLE"));


    private SampleH2Server() throws SQLException {
        loadServer();
    }

    public static SampleH2Server of() throws SQLException {
        return new SampleH2Server();
    }

    /**
     * See http://www.h2database.com/html/features.html#database_url
     */
    public static DBConnectionInfo getConnectionInfo() {
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
    void loadServer() throws SQLException {
        SampleGeoDB.of(this).createAndLoadTables();
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
        select(SQL.of("SELECT COUNT(*) FROM " + table));
        select(SQL.of("SELECT * FROM " + table));
    }

    static void update(SQL sql) throws SQLException {
        final DBConnectionInfo info = getConnectionInfo();
        Connection connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
    }

    static void select(SQL sql) throws SQLException {
        final DBConnectionInfo info = getConnectionInfo();
        Connection connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
    }

}

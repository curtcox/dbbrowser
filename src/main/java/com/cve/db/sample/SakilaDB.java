package com.cve.db.sample;

import com.cve.db.ConnectionInfo;
import com.cve.db.Database;
import com.cve.db.SQL;
import com.cve.db.dbio.DBConnection;
import com.cve.util.Check;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Creates and loads a sample H2 sakila server.
 * @author Curt
 */
public final class SakilaDB {

    static final Database SAKILA = Database.serverName(SampleServer.SAMPLE, "SAKILA");

    /**
     * How we connect to it.
     */
    private static final DBConnection connection = SampleServer.getConnection();

    /**
     * The static initializer does all the work -- once.
     */
    public static void load() {}

    static {
        loadDatabase();
    }

    static void loadDatabase() {
        try {
            SampleServer.createSchema(SAKILA);
            createTables();
            loadTables();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void createTables() throws SQLException, IOException {
        SQL[] sqls = resource("/h2-sakila/h2-sakila-schema.sql");
        for (SQL sql : sqls) {
            //System.out.print(sql);
            update(sql);
        }
    }

    static void loadTables() throws SQLException, IOException {
        SQL[] sqls = resource("/h2-sakila/h2-sakila-data.sql");
        for (SQL sql : sqls) {
            //System.out.print(sql);
            update(sql);
        }
    }

    static SQL[] resource(String name) throws IOException {
        InputStream in = Check.notNull(SakilaDB.class.getResourceAsStream(name),name);
        List<SQL> sqls = Lists.newArrayList();
        BufferedReader lines = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        for (String line = lines.readLine(); line!=null; line = lines.readLine()) {
            if (!line.startsWith("--")) {
                out.append(line + "\r\n");
                if (line.equals(";") || line.equals(");") || line.equals("COMMIT;")) {
                    sqls.add(SQL.of(out.toString()));
                    out = new StringBuilder();
                }
            }
        }
        if (!out.toString().isEmpty()) {
            sqls.add(SQL.of(out.toString()));
        }

        return sqls.toArray(new SQL[0]);
    }

    static void update(SQL sql) throws SQLException {
        ConnectionInfo info = connection.info;
        Connection connection = DriverManager.getConnection(info.url.toString(), info.user, info.password);
        Statement statement = connection.createStatement();
        statement.execute(sql.toString());
    }

    public static void main(String[] args) {
        load();
        System.exit(0);
    }
}

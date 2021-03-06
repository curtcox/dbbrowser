package com.cve.sample.db;

import com.cve.model.db.Database;
import com.cve.model.db.SQL;
import com.cve.util.Check;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

/**
 * Creates and loads a sample H2 sakila server.
 * @author Curt
 */
public final class SakilaDB {

    final SampleH2Server h2;

    public static final Database SAKILA = Database.serverName(SampleH2Server.SAMPLE, "SAKILA");

    private SakilaDB(SampleH2Server h2) {
        this.h2 = h2;
    }

    public static SakilaDB of(SampleH2Server h2) {
        return new SakilaDB(h2);
    }

    void createAndLoadTables() throws SQLException, IOException {
        h2.createSchema(SAKILA);
        createTables();
        loadTables();
    }

    private void createTables() throws SQLException, IOException {
        SQL[] sqls = resource("/h2-sakila/h2-sakila-schema.sql");
        for (SQL sql : sqls) {
            //System.out.print(sql);
            update(sql);
        }
    }

    private void loadTables() throws SQLException, IOException {
        SQL[] sqls = resource("/h2-sakila/h2-sakila-data.sql");
        for (SQL sql : sqls) {
            //System.out.print(sql);
            update(sql);
        }
    }

    private static SQL[] resource(String name) throws IOException {
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

    private void update(SQL sql) throws SQLException {
        h2.update(sql);
    }

}

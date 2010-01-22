package com.cve.io.db;

import com.cve.log.Log;
import com.cve.model.db.SQL;
import com.cve.sample.db.SampleH2Server;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class DBResultSetIOTest {

    Log log;

    /**
        GEO.tableName cities
        city_id INT, name VARCHAR(255), state_id INT, population INT
            1        "Chicago"             1             3000000
            2        "Lincoln"             1               20000
            3        "Virginia"            1                1500
            4        "Mason City"          1                1500
            5        "Springfield"         1              100000
     * @throws SQLException
     */
    @Test
    public void cities() throws Exception {
        SampleH2Server.of();
        SQL sql = SQL.of("SELECT * FROM GEO.CITIES");
        ResultSet results = SampleH2Server.select(sql);
        DBResultSetIO io = DBResultSetIO.of(results,log);
        assertEquals(4,io.meta.columnCount);

        ImmutableList<ImmutableMap> rows = io.rows;
        assertEquals(5,        rows.size());
        assertEquals(1        ,rows.get(0).get(0));
        assertEquals("Chicago",rows.get(0).get(1));
        assertEquals("Chicago",rows.get(0).get("NAME"));
        assertEquals(100000,rows.get(4).get(3));
        assertEquals(100000,rows.get(4).get("POPULATION"));
    }

    public static void main(String[] args) throws Exception {
        SampleH2Server.of();
        DBResultSetIOTest test = new DBResultSetIOTest();
        test.cities();
        System.out.println("Done.");
        System.exit(0);
    }
}

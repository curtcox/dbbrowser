package com.cve.io.db;

import com.cve.model.db.SQL;
import com.cve.sample.db.SampleH2Server;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class DBResultSetMetaDataIOTest {

    ;

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
        ResultSet results = SampleH2Server.of().select(sql);
        DBResultSetIO io = DBResultSetIO.of(results);
        DBResultSetMetaDataIO meta = io.meta;
        assertEquals(4,        meta.columnCount);

        assertEquals("CITY_ID"     ,meta.columnNames.get(0));
        assertEquals("NAME"        ,meta.columnNames.get(1));
        assertEquals("STATE_ID"    ,meta.columnNames.get(2));
        assertEquals("POPULATION"  ,meta.columnNames.get(3));

        assertEquals("CITIES"      ,meta.tableNames.get(0));
        assertEquals("CITIES"      ,meta.tableNames.get(1));
        assertEquals("CITIES"      ,meta.tableNames.get(2));
        assertEquals("CITIES"      ,meta.tableNames.get(3));

        assertEquals("GEO"        ,meta.schemaNames.get(0));
        assertEquals("GEO"        ,meta.schemaNames.get(1));
        assertEquals("GEO"        ,meta.schemaNames.get(2));
        assertEquals("GEO"        ,meta.schemaNames.get(3));

        assertEquals("SAMPLE"     ,meta.catalogNames.get(0));
        assertEquals("SAMPLE"     ,meta.catalogNames.get(1));
        assertEquals("SAMPLE"     ,meta.catalogNames.get(2));
        assertEquals("SAMPLE"     ,meta.catalogNames.get(3));
    }

    public static void main(String[] args) throws Exception {
        SampleH2Server.of();
        DBResultSetMetaDataIOTest test = new DBResultSetMetaDataIOTest();
        test.cities();
        System.out.println("Done.");
        System.exit(0);
    }
}

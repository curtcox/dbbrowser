package com.cve.model.db;

import com.cve.log.Log;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class TableTest {

    Log log;

    public TableTest() {}

    @Test
    public void equality() {
        DBServer server = DBServer.uri(URIs.of("server"),log);
        Database   db = server.databaseName("db");
        DBTable   table1 = db.tableName("foo");
        DBTable   table2 = db.tableName("foo");
        assertEquals(table1,table2);
    }


}
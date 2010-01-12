package com.cve.db;

import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class TableTest {

    public TableTest() {}

    @Test
    public void equality() {
        DBServer server = DBServer.uri(URIs.of("server"));
        Database   db = server.databaseName("db");
        DBTable   table1 = db.tableName("foo");
        DBTable   table2 = db.tableName("foo");
        assertEquals(table1,table2);
    }


}
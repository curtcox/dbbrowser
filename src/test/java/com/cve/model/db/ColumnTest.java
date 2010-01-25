package com.cve.model.db;

import com.cve.log.Log;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ColumnTest {

    Log log;

    public ColumnTest() {}

    @Test
    public void equality() {
        DBServer server = DBServer.uri(URIs.of("server"),log);
        Database   db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        assertEquals(DBColumn.tableNameType(table,"bar", String.class),
                     DBColumn.tableNameType(table,"bar", String.class));
    }


    @Test
    public void unequality() {
        DBServer server = DBServer.uri(URIs.of("server"),log);
        Database   db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        DBColumn bar = DBColumn.tableName(table,"bar");
        DBColumn baz = DBColumn.tableName(table,"baz");
        assertFalse(bar.equals(baz));
    }


}
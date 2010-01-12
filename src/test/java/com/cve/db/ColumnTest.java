package com.cve.db;

import com.cve.model.db.Database;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ColumnTest {

    public ColumnTest() {}

    @Test
    public void equality() {
        DBServer server = DBServer.uri(URIs.of("server"));
        Database   db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        assertEquals(DBColumn.tableNameType(table,"bar", String.class),
                     DBColumn.tableNameType(table,"bar", String.class));
    }


    @Test
    public void unequality() {
        DBServer server = DBServer.uri(URIs.of("server"));
        Database   db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        DBColumn bar = DBColumn.tableName(table,"bar");
        DBColumn baz = DBColumn.tableName(table,"baz");
        assertFalse(bar.equals(baz));
    }


}
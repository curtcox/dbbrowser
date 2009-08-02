package com.cve.db;

import com.cve.db.Database;
import com.cve.db.DBColumn;
import com.cve.db.Server;
import com.cve.db.DBTable;
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
        Server server = Server.uri(URIs.of("server"));
        Database   db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        assertEquals(DBColumn.tableNameType(table,"bar", String.class),
                     DBColumn.tableNameType(table,"bar", String.class));
    }


    @Test
    public void unequality() {
        Server server = Server.uri(URIs.of("server"));
        Database   db = server.databaseName("db");
        DBTable   table = db.tableName("foo");
        DBColumn bar = DBColumn.tableName(table,"bar");
        DBColumn baz = DBColumn.tableName(table,"baz");
        assertFalse(bar.equals(baz));
    }


}
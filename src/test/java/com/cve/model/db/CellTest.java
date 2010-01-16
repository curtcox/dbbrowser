package com.cve.model.db;

import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBRow;
import com.cve.model.db.Cell;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class CellTest {

    public CellTest() {}


    @Test
    public void equalityWithSameValues() throws URISyntaxException {
        DBRow       row = DBRow.FIRST;
        DBTable table = DBTable.databaseName(DBServer.uri(new URI("")).databaseName("db"),"foo");
        DBColumn column = DBColumn.tableNameType(table,"bar", String.class);
        assertEquals(Cell.at(row, column), Cell.at(row, column));
    }

    @Test
    public void equalityWithEquivalentValues() throws URISyntaxException {
        DBTable table = DBTable.databaseName(DBServer.uri(new URI("")).databaseName("db"),"foo");
        DBRow       r1a = DBRow.FIRST.next();
        DBRow       r1b = DBRow.FIRST.next();
        DBColumn    c1a = DBColumn.tableNameType(table,"bar", String.class);
        DBColumn    c1b = DBColumn.tableNameType(table,"bar", String.class);
        assertEquals(Cell.at(r1a, c1a), Cell.at(r1b, c1b));
    }


}
package com.cve.model.db;

import com.cve.log.Log;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class CellTest {

    Log log;

    public CellTest() {}


    @Test
    public void equalityWithSameValues() throws URISyntaxException {
        DBRow       row = DBRow.FIRST;
        DBTable table = DBTable.databaseName(DBServer.uri(new URI(""),log).databaseName("db"),"foo",log);
        DBColumn column = DBColumn.tableNameType(table,"bar", String.class);
        assertEquals(Cell.at(row, column), Cell.at(row, column));
    }

    @Test
    public void equalityWithEquivalentValues() throws URISyntaxException {
        DBTable table = DBTable.databaseName(DBServer.uri(new URI(""),log).databaseName("db"),"foo",log);
        DBRow       r1a = DBRow.FIRST.next();
        DBRow       r1b = DBRow.FIRST.next();
        DBColumn    c1a = DBColumn.tableNameType(table,"bar", String.class);
        DBColumn    c1b = DBColumn.tableNameType(table,"bar", String.class);
        assertEquals(Cell.at(r1a, c1a), Cell.at(r1b, c1b));
    }


}
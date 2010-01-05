package com.cve.db;

import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class SelectResultsTest {

    public SelectResultsTest() {}


    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test
    public void selectGetterEqualsConstructor() {
        DBServer           server = DBServer.uri(URIs.of("server"));
        Database       database = server.databaseName("database");
        DBTable             table = database.tableName("table");
        DBColumn           column = DBColumn.tableNameType(table,"column",String.class);
        Select    givenSelect   = Select.from(database,table,column);
        DBResultSet givenResults  = DBResultSet.of(database,table,column);
        SelectResults results   = SelectResults.selectResultsHintsMore(givenSelect,givenResults,Hints.NONE,false);
        Select returnSelect = results.select;
        assertEquals(givenSelect, returnSelect);
    }


}
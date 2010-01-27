package com.cve.model.db;

import com.cve.log.Log;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class SelectResultsTest {

    Log log;

    public SelectResultsTest() {}


    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test
    public void selectGetterEqualsConstructor() {
        DBServer           server = DBServer.uri(URIs.of("server"),log);
        Database       database = server.databaseName("database");
        DBTable             table = database.tableName("table");
        DBColumn           column = DBColumn.tableNameType(table,"column",String.class);
        Select    givenSelect   = Select.from(database,table,column);
        DBResultSet givenResults  = DBResultSet.of(database,table,column,log);
        SelectResults results   = SelectResults.selectResultsHintsMore(givenSelect,givenResults,Hints.NONE,false,log);
        Select returnSelect = results.select;
        assertEquals(givenSelect, returnSelect);
    }


}
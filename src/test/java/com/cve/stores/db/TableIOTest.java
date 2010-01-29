package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;

/**
 *
 * @author Curt
 */
public class TableIOTest {

    ;
    TableIO io = TableIO.of();

    @Test
    public void both() {
        both(DBServer.uri(URIs.of("one")).databaseName("db").tableName("t1"));
        both(DBServer.uri(URIs.of("two")).databaseName("db").tableName("t2"));
    }

    void both(DBTable t) {
         assertEquals(t, io.read(io.write(t)));
    }

    private void assertEquals(DBTable t, DBTable parse) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

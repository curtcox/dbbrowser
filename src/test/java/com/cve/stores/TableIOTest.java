package com.cve.stores;

import com.cve.db.DBTable;
import com.cve.db.Server;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class TableIOTest {

    static TableIO io = TableIO.of();

    @Test
    public void both() {
        both(Server.uri(URIs.of("one")).databaseName("db").tableName("t1"));
        both(Server.uri(URIs.of("two")).databaseName("db").tableName("t2"));
    }

    void both(DBTable t) {
         assertEquals(t, io.parse(io.format(t)));
    }

    private void assertEquals(DBTable t, DBTable parse) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

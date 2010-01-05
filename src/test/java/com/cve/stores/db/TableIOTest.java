package com.cve.stores.db;

import com.cve.stores.db.TableIO;
import com.cve.db.DBTable;
import com.cve.db.DBServer;
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

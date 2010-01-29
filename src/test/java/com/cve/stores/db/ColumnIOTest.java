package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class ColumnIOTest {

    ;

    ColumnIO io = ColumnIO.of();

    @Test
    public void both() {
        both(DBServer.uri(URIs.of("one")).databaseName("db").tableName("t1").columnName("c1"));
        both(DBServer.uri(URIs.of("two")).databaseName("db").tableName("t2").columnName("c2"));
    }

    void both(DBColumn c) {
         assertEquals(c, io.read(io.write(c)));
    }

}

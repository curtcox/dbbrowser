package com.cve.stores.db;

import com.cve.stores.db.ColumnIO;
import com.cve.db.DBColumn;
import com.cve.db.Server;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class ColumnIOTest {

    static ColumnIO io = ColumnIO.of();

    @Test
    public void both() {
        both(Server.uri(URIs.of("one")).databaseName("db").tableName("t1").columnName("c1"));
        both(Server.uri(URIs.of("two")).databaseName("db").tableName("t2").columnName("c2"));
    }

    void both(DBColumn c) {
         assertEquals(c, io.read(io.write(c)));
    }

}

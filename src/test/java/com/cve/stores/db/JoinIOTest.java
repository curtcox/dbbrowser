package com.cve.stores.db;

import com.cve.stores.db.JoinIO;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Join;
import com.cve.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class JoinIOTest {

    static JoinIO io = JoinIO.of();

    @Test
    public void both() {
        Database db = DBServer.uri(URIs.of("one")).databaseName("db");
        DBColumn c1 = db.tableName("t1").columnName("c1");
        DBColumn c2 = db.tableName("t2").columnName("c2");
        Join join = Join.of(c1, c2);
        both(join);
    }

    void both(Join j) {
         assertEquals(j, io.read(io.write(j)));
    }

}

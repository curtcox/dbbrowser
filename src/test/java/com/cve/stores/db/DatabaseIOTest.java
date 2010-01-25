package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class DatabaseIOTest {

    Log log;

    DatabaseIO io = DatabaseIO.of(log);

    @Test
    public void both() {
        both(DBServer.uri(URIs.of("one"),log).databaseName("db"));
        both(DBServer.uri(URIs.of("two"),log).databaseName("db"));
    }

    void both(Database db) {
         assertEquals(db, io.read(io.write(db)));
    }

}

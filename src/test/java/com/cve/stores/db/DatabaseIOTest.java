package com.cve.stores.db;

import com.cve.stores.db.DatabaseIO;
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

    static DatabaseIO io = DatabaseIO.of();

    @Test
    public void both() {
        both(DBServer.uri(URIs.of("one")).databaseName("db"));
        both(DBServer.uri(URIs.of("two")).databaseName("db"));
    }

    void both(Database db) {
         assertEquals(db, io.read(io.write(db)));
    }

}

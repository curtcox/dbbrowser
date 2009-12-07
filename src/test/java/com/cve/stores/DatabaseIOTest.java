package com.cve.stores;

import com.cve.db.Database;
import com.cve.db.Server;
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
        both(Server.uri(URIs.of("one")).databaseName("db"));
        both(Server.uri(URIs.of("two")).databaseName("db"));
    }

    void both(Database db) {
         assertEquals(db, io.parse(io.format(db)));
    }

}

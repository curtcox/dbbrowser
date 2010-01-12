package com.cve.stores.db;

import com.cve.stores.db.ServerIO;
import com.cve.model.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class ServerIOTest {

    static ServerIO io = ServerIO.of();

    @Test
    public void both() {
        both(DBServer.uri(URIs.of("one")));
        both(DBServer.uri(URIs.of("one")));
    }

    void both(DBServer s) {
         assertEquals(s, io.read(io.write(s)));
    }

}

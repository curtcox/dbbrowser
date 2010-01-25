package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class ServerIOTest {

    Log log;

    ServerIO io = ServerIO.of(log);

    @Test
    public void both() {
        both(DBServer.uri(URIs.of("one"),log));
        both(DBServer.uri(URIs.of("one"),log));
    }

    void both(DBServer s) {
         assertEquals(s, io.read(io.write(s)));
    }

}

package com.cve.db;

import com.cve.model.db.DBServer;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ServerTest {

    public ServerTest() {}

    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test
    public void equality() {
        DBServer server1 = DBServer.uri(URIs.of("server"));
        DBServer server2 = DBServer.uri(URIs.of("server"));
        assertEquals(server1,server2);
    }


}
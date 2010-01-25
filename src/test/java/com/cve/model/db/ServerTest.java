package com.cve.model.db;

import com.cve.log.Log;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ServerTest {

    Log log;

    public ServerTest() {}

    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test
    public void equality() {
        DBServer server1 = DBServer.uri(URIs.of("server"),log);
        DBServer server2 = DBServer.uri(URIs.of("server"),log);
        assertEquals(server1,server2);
    }


}
package com.cve.db;

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
        Server server1 = Server.uri(URIs.of("server"));
        Server server2 = Server.uri(URIs.of("server"));
        assertEquals(server1,server2);
    }


}
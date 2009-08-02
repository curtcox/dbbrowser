package com.cve.db;

import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class DatabaseTest {

    public DatabaseTest() {}


    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test
    public void equality() {
        Server           server = Server.uri(URIs.of("server"));
        assertEquals(server.databaseName("foo"), server.databaseName("foo"));
    }


}
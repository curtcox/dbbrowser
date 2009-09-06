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
     */
    @Test
    public void equality() {
        Server           server = Server.uri(URIs.of("server"));
        assertEquals(server.databaseName("foo"), server.databaseName("foo"));
    }

    /**
     */
    @Test
    public void notEqualWhenServersDiffer() {
        Server           server1 = Server.uri(URIs.of("server1"));
        Server           server2 = Server.uri(URIs.of("server2"));
        assertNotEquals(server1.databaseName("foo"), server2.databaseName("foo"));
    }

    @Test
    public void notEqualWhenNamesDiffer() {
        Server           server = Server.uri(URIs.of("server"));
        assertNotEquals(server.databaseName("foo"), server.databaseName("bar"));
    }

    static void assertNotEquals(Object a, Object b) {
        assertFalse(a.equals(b));
    }
}
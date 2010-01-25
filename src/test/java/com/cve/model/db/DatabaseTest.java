package com.cve.model.db;

import com.cve.log.Log;
import com.cve.util.URIs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class DatabaseTest {

    Log log;

    public DatabaseTest() {}


    /**
     */
    @Test
    public void equality() {
        DBServer           server = DBServer.uri(URIs.of("server"),log);
        assertEquals(server.databaseName("foo"), server.databaseName("foo"));
    }

    /**
     */
    @Test
    public void notEqualWhenServersDiffer() {
        DBServer           server1 = DBServer.uri(URIs.of("server1"),log);
        DBServer           server2 = DBServer.uri(URIs.of("server2"),log);
        assertNotEquals(server1.databaseName("foo"), server2.databaseName("foo"));
    }

    @Test
    public void notEqualWhenNamesDiffer() {
        DBServer           server = DBServer.uri(URIs.of("server"),log);
        assertNotEquals(server.databaseName("foo"), server.databaseName("bar"));
    }

    static void assertNotEquals(Object a, Object b) {
        assertFalse(a.equals(b));
    }
}
package com.cve.web.db;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class FreeFormQueryHandlerTest {

    final FreeFormQueryHandler handler = new FreeFormQueryHandler();

    @Test
    public void doesntHandleServerWithNoQuery() {
        assertFalse(handler.handles("/server/"));
        assertFalse(handler.handles("/server"));
    }

    @Test
    public void handlesQueryWithJustServer() {
        assertTrue(handler.handles("/server/select?q=*+from+bar"));
    }

    @Test
    public void handlesQueryWithServerAndDatabase() {
        assertTrue(handler.handles("/server/database/select?q=*+from+bar"));
    }

    @Test
    public void doesntHandleServerAndDatabaseWithNoQuery() {
        assertFalse(handler.handles("/server/database/"));
        assertFalse(handler.handles("/server/database"));
    }

}

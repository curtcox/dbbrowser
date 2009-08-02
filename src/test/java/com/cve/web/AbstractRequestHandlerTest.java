package com.cve.web;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class AbstractRequestHandlerTest {

    static class TestHandler extends AbstractRequestHandler {
        TestHandler(String regexp) { super(regexp); }
        public PageResponse doProduce(PageRequest request) throws IOException, SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Test
    public void exitRegexpHandlesExit() throws IOException {
        TestHandler handler = new TestHandler("/exit");
        assertTrue(handler.handles("^/exit"));
    }

    @Test
    public void exitRegexpHandlesThingsThatStartWithExit() throws IOException {
        TestHandler handler = new TestHandler("^/exit");
        assertTrue(handler.handles("/exit/stillHandleThis"));
        assertTrue(handler.handles("/exit?andthis"));
        assertTrue(handler.handles("/exit?and=this"));
    }

    @Test
    public void exitRegexpOnlyHandlesThingsThatStartWithExit() throws IOException {
        TestHandler handler = new TestHandler("^/exit");
        assertFalse(handler.handles("/shmexit"));
        assertFalse(handler.handles("/booger"));
        assertFalse(handler.handles("/slash/exit"));
    }

}

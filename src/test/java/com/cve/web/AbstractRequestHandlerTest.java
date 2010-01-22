package com.cve.web;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class AbstractRequestHandlerTest {

    static class TestHandler extends AbstractRequestHandler {
        TestHandler(String regexp) { super(regexp,null); }
        @Override
        public Model get(PageRequest request) {
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

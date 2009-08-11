package com.cve.web;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class CompositeRequestHandlerTest {

    /**
     * For producing composite handlers that just return their regexp as a
     * model.
     */
    static RequestHandler newHandler(String... regexps) {
        RequestHandler[] handlers = new RequestHandler[regexps.length];
        for (int i=0; i<regexps.length; i++) {
            handlers[i] = new TestHandler(regexps[i]);
        }
        return CompositeRequestHandler.of(handlers);
    }

    static class TestHandler extends AbstractRequestHandler {
        final String regexp;
        TestHandler(String regexp) {
            super(regexp);
            this.regexp = regexp;
        }
        public PageResponse get(PageRequest request) throws IOException, SQLException {
            return PageResponse.of(new StringModel(regexp));
        }
    }

    @Test
    public void oneHandlesOne() throws IOException, SQLException {
        String one = "/one";
        RequestHandler handler = newHandler(one);
        StringModel model = (StringModel) handler.produce(PageRequest.path(one)).getModel();
        assertEquals(one,model.string);
    }

    @Test
    public void oneDoesntHandleTwo() throws IOException, SQLException {
        String one = "/one";
        String two = "/two";
        RequestHandler handler = newHandler(one);
        PageResponse response = handler.produce(PageRequest.path(two));
        assertNull(response);
    }

}

package com.cve.web.core.handlers;

import com.cve.lang.RegEx;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.models.StringModel;
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
            super(RegEx.of(regexp));
            this.regexp = regexp;
        }
        @Override
        public StringModel get(PageRequest request) {
            return new StringModel(regexp);
        }
    }

    @Test
    public void oneHandlesOne() throws IOException, SQLException {
        String one = "/one";
        RequestHandler handler = newHandler(one);
        StringModel model = (StringModel) handler.produce(PageRequest.path(one)).model;
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

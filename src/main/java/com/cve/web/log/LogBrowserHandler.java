package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.*;

/**
 * The {@link RequestHandler} for browsing objects in the log.
 * @author Curt
 */
public final class LogBrowserHandler implements RequestHandler {

    private final RequestHandler handler;

    private LogBrowserHandler() {
        handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        ObjectBrowserHandler.of(),      // /object/
        RequestBrowserHandler.of());       // /request/
    }

    public static RequestHandler of() {
        return new LogBrowserHandler();
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

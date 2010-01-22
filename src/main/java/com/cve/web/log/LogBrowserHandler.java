package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.*;

/**
 * The {@link RequestHandler} for browsing objects in the log.
 * @author Curt
 */
public final class LogBrowserHandler implements RequestHandler {

    private final RequestHandler handler;

    private LogBrowserHandler(Log log) {
        handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        ObjectBrowserHandler.of(log));       // /object/
    }

    public static RequestHandler of(Log log) {
        return new LogBrowserHandler(log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

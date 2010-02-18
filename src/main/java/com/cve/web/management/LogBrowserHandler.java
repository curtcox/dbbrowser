package com.cve.web.management;

import com.cve.web.core.PageResponse;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.handlers.PrefixMapRequestHandler;

/**
 * The {@link RequestHandler} for browsing objects in the log.
 * @author Curt
 */
public final class LogBrowserHandler implements RequestHandler {

    private final RequestHandler handler;

    private LogBrowserHandler() {
        handler = PrefixMapRequestHandler.of(
        // URLs of the form handler
        "^/object" , ObjectBrowserHandler.of(),
        "^/request", PageRequestBrowserHandler.of()); 
    }

    public static RequestHandler of() {
        return new LogBrowserHandler();
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

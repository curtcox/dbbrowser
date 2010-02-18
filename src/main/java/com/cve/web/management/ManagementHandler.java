package com.cve.web.management;

import com.cve.web.core.PageResponse;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.handlers.PrefixMapRequestHandler;

/**
 * The {@link RequestHandler} for browsing objects in the log.
 * @author Curt
 */
public final class ManagementHandler implements RequestHandler {

    private final RequestHandler handler;

    private ManagementHandler() {
        handler = PrefixMapRequestHandler.of(
        // for URLs of the form
        "^/jmx",       JMXPage.of(),
        "^/object",    ObjectBrowserHandler.of(),
        "^/request",   PageRequestBrowserHandler.of());
    }

    public static RequestHandler of() { return new ManagementHandler(); }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

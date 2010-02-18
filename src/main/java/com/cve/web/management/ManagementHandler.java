package com.cve.web.management;

import com.cve.web.core.PageResponse;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.handlers.CompositeRequestHandler;
import com.cve.web.*;

/**
 * The {@link RequestHandler} for browsing objects in the log.
 * @author Curt
 */
public final class ManagementHandler implements RequestHandler {

    private final RequestHandler handler;

    private ManagementHandler() {
        handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        ManagementFactoryHandler.of(),     // /jmx/
        ObjectBrowserHandler.of(),         // /object/
        PageRequestBrowserHandler.of());   // /request/
    }

    public static RequestHandler of() {
        return new ManagementHandler();
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

package com.cve.web.log;

import com.cve.web.*;

/**
 * The {@link RequestHandler} for browsing objects in the log.
 * @author Curt
 */
public final class LogBrowserHandler {

    private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        new ObjectBrowserHandler());       // /object/

    public static RequestHandler newInstance() {
        return handler;
    }

}

package com.cve.web;

import java.io.IOException;
import static com.cve.log.Log.args;

import static com.cve.util.Check.notNull;
import java.sql.SQLException;

/**
 * A wrapper for other requests to give debugging.
 */
public final class DebugHandler implements RequestHandler {

    private static ThreadLocal<Boolean> debug = new ThreadLocal();

    /**
     * The thing that really handles the requests
     */
    private final RequestHandler handler;

    private DebugHandler(RequestHandler handler) {
        this.handler = notNull(handler);
    }

    public static RequestHandler of(RequestHandler handler) {
        return new DebugHandler(handler);
    }

    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        args(request);
        String requestURI = request.requestURI;
        if (!requestURI.startsWith("/*")) {
            debug.set(Boolean.FALSE);
            return handler.produce(request);
        }
        debug.set(Boolean.TRUE);
        PageRequest stripped = request.withURI("/" + requestURI.substring(2));
        return handler.produce(stripped);
    }

    public static boolean isOn() {
        return debug.get();
    }
}

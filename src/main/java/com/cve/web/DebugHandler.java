package com.cve.web;

import static com.cve.log.Log.args;

import static com.cve.util.Check.notNull;

/**
 * A wrapper for other requests to give debugging.
 */
public final class DebugHandler implements RequestHandler {

    private static final String PREFIX = "/&";

    /**
     * Is this thread debugging?
     * Right now, debugging is on or off per-request and not server-wide.
     * This has pros and cons, so we might want to revisit this decision
     * later.
     * pros
     *     - only debug what we need to, so we don't incur the costs and
     *       modify the behaviour for all requests
     * cons
     *     - we could change the behaviour of the page we are trying to debug
     *       by mucking with the URLs
     */
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
    public PageResponse produce(PageRequest request) {
        args(request);
        String requestURI = request.requestURI;
        if (!requestURI.startsWith(PREFIX)) {
            debug.set(Boolean.FALSE);
            return handler.produce(request);
        }
        debug.set(Boolean.TRUE);
        PageRequest stripped = request.withURI("/" + requestURI.substring(2));
        return handler.produce(stripped);
    }

    public static boolean isOn() {
        Boolean on = debug.get();
        if (on==null) {
            return false;
        }
        return on;
    }
}

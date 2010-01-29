package com.cve.web;

import com.cve.util.AnnotatedStackTrace;
import com.cve.web.log.ObjectLink;
import com.cve.log.Log;
import com.cve.log.Logs;

import static com.cve.util.Check.notNull;

/**
 * A wrapper for other requests to give debugging.
 * Note that this handler doesn't add any debugging itself.
 * It looks for URLs that start with an ampersand as an indicator that debugging
 * should occur.  The wrapped handler is always given the URL without the
 * initial ampersand.  It can use the static methods on this class to check
 * debugging status, or add debugging info.
 */
public final class DebugHandler implements RequestHandler {

    private final Log log = Logs.of();

    /**
     * Only pay attention to requests that start with this.
     */
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

    /**
     * Use the factory.
     */
    private DebugHandler(RequestHandler handler) {
        this.handler = notNull(handler);
        
    }

    /**
     * Create a new DebugHandler, wrapping the given handler.
     */
    public static RequestHandler of(RequestHandler handler) {
        return new DebugHandler(handler);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        String requestURI = request.requestURI;
        if (!requestURI.startsWith(PREFIX)) {
            debug.set(Boolean.FALSE);
            return handler.produce(request);
        }
        debug.set(Boolean.TRUE);
        PageRequest stripped = request.withURI("/" + requestURI.substring(2));
        return handler.produce(stripped);
    }

    /**
     * Is debugging on for this request.
     * @return
     */
    public static boolean isOn() {
        Boolean on = debug.get();
        if (on==null) {
            return false;
        }
        return on;
    }

    /**
     * Return a debugging link, if debugging is on.
     * @return
     */
    public static String debugLink() {
        if (!DebugHandler.isOn()) {
            return "";
        }
        AnnotatedStackTrace trace = Logs.of().annotatedStackTrace();
        int max = 200;
        if (trace.elements.size() > max) {
            String message = "The maximum stack depth of " + max +
               " has been passed.  This exception is being thrown to short-circuit" +
               " what looks like endless recursion."
            ;
            throw new IllegalStateException(message);
        }
        return ObjectLink.of().to(".",trace);
    }

}

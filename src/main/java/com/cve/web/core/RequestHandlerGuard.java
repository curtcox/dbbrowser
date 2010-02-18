package com.cve.web.core;


import com.cve.log.Log;
import com.cve.log.Logs;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For allowing conditional access to a handler.
 * This could be used to guard admin functions, debugging, etc...
 * @author Curt
 */
@Immutable
public abstract class RequestHandlerGuard implements RequestHandler {

    /**
     * Defer to this
     */
    private final RequestHandler handler;

    private final Log log = Logs.of();

    protected RequestHandlerGuard(RequestHandler handler) {
        this.handler = notNull(handler);
        
    }

    /**
     * Return a new guard that always makes the same decisions.
     */
    public static RequestHandler of(RequestHandler handler, final boolean pass, final URI denied) {
        return new RequestHandlerGuard(handler) {
            @Override public boolean passes(PageRequest request) {
                return pass;
            }

            @Override public URI getDeniedURI(PageRequest request) {
                return denied;
            }

        };
    }

    @Override
    public PageResponse produce(PageRequest request) {
        if (passes(request)) {
            return handler.produce(request);
        }
        URI dest = getDeniedURI(request);
        return PageResponse.newRedirect(request,dest);
    }

    /**
     * Return true if the given request should be allowed to pass to the
     * guarded handler.
     */
    public abstract boolean passes(PageRequest request);

    /**
     * Return the URI to redirect to when access is denied.
     */
    public abstract URI getDeniedURI(PageRequest request);
}

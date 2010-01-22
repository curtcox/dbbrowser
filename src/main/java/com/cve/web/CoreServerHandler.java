package com.cve.web;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;

/**
 * Request handlers not having to do with anything specific, like
 * databases, logs, or object browsing.
 * @author Curt
 */
public class CoreServerHandler implements RequestHandler {

    final Log log;

    final RequestHandler handler;

    private CoreServerHandler(Log log) {
        this.log = notNull(log);
        handler = CompositeRequestHandler.of(
            // handler                         // for URLs of the form
            ExitHandler.of(),         // /exit
            ResourceHandler.of(log),     // /resource
            UserLoginHandler.of(log),            // /login
            UserLogoutHandler.of(log)            // /logout
        );
    }

    public static CoreServerHandler of(Log log) {
        return new CoreServerHandler(log);
    }
    
    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }
}
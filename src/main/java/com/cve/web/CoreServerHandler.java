package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;

/**
 * Request handlers not having to do with anything specific, like
 * databases, logs, or object browsing.
 * @author Curt
 */
public class CoreServerHandler implements RequestHandler {

    final Log log = Logs.of();

    final RequestHandler handler;

    private CoreServerHandler() {
        
        handler = CompositeRequestHandler.of(
            // handler                         // for URLs of the form
            ExitHandler.of(),         // /exit
            ResourceHandler.of(),     // /resource
            UserLoginHandler.of(),            // /login
            UserLogoutHandler.of()            // /logout
        );
    }

    public static CoreServerHandler of() {
        return new CoreServerHandler();
    }
    
    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }
}
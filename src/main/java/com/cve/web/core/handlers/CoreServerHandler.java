package com.cve.web.core.handlers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;

/**
 * Request handlers not having to do with anything specific, like
 * databases, logs, or object browsing.
 * @author Curt
 */
public final class CoreServerHandler implements RequestHandler {

    final Log log = Logs.of();

    final RequestHandler handler;

    private CoreServerHandler() {
        
        handler = PrefixMapRequestHandler.of(
             // for URLs of the form handler
            "^/exit",      ExitHandler.of(),
            "^/resource/", ResourceHandler.of(),
            "^/login",     UserLoginHandler.of(),
            "^/logout",    UserLogoutHandler.of()
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
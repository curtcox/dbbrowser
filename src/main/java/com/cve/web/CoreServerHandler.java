package com.cve.web;

/**
 * Request handlers not having to do with anything specific, like
 * databases, logs, or object browsing.
 * @author Curt
 */
public class CoreServerHandler {

   private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        ExitHandler.of(),         // /exit
        ResourceHandler.of(),     // /resource
        UserLoginHandler.of(),            // /login
        UserLogoutHandler.of()            // /logout
    );

    public static RequestHandler of() {
        return handler;
    }

}

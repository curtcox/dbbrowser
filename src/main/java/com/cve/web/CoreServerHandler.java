package com.cve.web;

/**
 * Request handlers not having to do with anything specific, like
 * databases, logs, or object browsing.
 * @author Curt
 */
public class CoreServerHandler {

   private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        ExitHandler.newInstance(),         // /exit
        ResourceHandler.newInstance(),     // /resource
        new UserLoginHandler(),            // /login
        new UserLogoutHandler()            // /logout
    );

    public static RequestHandler newInstance() {
        return handler;
    }

}

package com.cve.web.db.servers;

import com.cve.web.db.*;
import com.cve.web.*;

/**
 * The {@link RequestHandler} for requests that just specify the 
 * database server.
 * @author Curt
 */
public final class DBServersHandler {

    private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        new ServersHandler(),              // /
        new AddServerHandler(),            // /add
        new RemoveServerHandler(),         // /remove
        DatabaseMetaHandler.newInstance()  // /meta/server/
    );

    public static RequestHandler newInstance() {
        return handler;
    }

}

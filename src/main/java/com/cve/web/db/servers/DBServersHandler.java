package com.cve.web.db.servers;

import com.cve.web.core.PageResponse;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;
import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.web.db.*;
import com.cve.web.core.handlers.PrefixMapRequestHandler;

/**
 * The {@link RequestHandler} for requests that just specify the 
 * database server.
 * @author Curt
 */
public final class DBServersHandler implements RequestHandler {

    private final RequestHandler handler;

    private DBServersHandler(DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        handler = PrefixMapRequestHandler.of(
            // URLs of the form           handler
            "^/",           ServersHandler.of(db,serversStore),
            "^/add",        AddServerHandler.of(serversStore),
            "^/remove",     RemoveServerHandler.of(serversStore),
            "^/meta/",      DatabaseMetaHandler.of(db,serversStore,managedFunction)
        );
    }

    public static DBServersHandler of(DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new DBServersHandler(db,serversStore,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

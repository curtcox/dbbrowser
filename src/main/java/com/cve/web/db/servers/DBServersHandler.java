package com.cve.web.db.servers;

import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.web.db.*;
import com.cve.web.*;

/**
 * The {@link RequestHandler} for requests that just specify the 
 * database server.
 * @author Curt
 */
public final class DBServersHandler implements RequestHandler {

    private final RequestHandler handler;

    private DBServersHandler(DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        handler = CompositeRequestHandler.of(
            // handler                         // for URLs of the form
            ServersHandler.of(db,serversStore),             // /
            AddServerHandler.of(serversStore),            // /add
            new RemoveServerHandler(),         // /remove
            DatabaseMetaHandler.of(db,serversStore,managedFunction)  // /meta/server/
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

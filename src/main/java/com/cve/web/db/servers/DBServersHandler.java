package com.cve.web.db.servers;

import com.cve.db.dbio.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.web.db.*;
import com.cve.web.*;

/**
 * The {@link RequestHandler} for requests that just specify the 
 * database server.
 * @author Curt
 */
public final class DBServersHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    private DBServersHandler(DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.db = db;
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
    }

    public static DBServersHandler of(DBMetaData.Factory db, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new DBServersHandler(db,serversStore,managedFunction);
    }

    public RequestHandler of() {
        return CompositeRequestHandler.of(
            // handler                         // for URLs of the form
            ServersHandler.of(db,serversStore),             // /
            AddServerHandler.of(serversStore),            // /add
            new RemoveServerHandler(),         // /remove
            DatabaseMetaHandler.of(db,serversStore,managedFunction)  // /meta/server/
        );
    }

}

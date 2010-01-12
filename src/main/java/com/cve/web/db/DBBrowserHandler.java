package com.cve.web.db;

import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.db.databases.DatabasesHandler;
import com.cve.web.*;
import com.cve.web.db.servers.DBServersHandler;

/**
 * The {@link RequestHandler} for browsing databases.
 * @author Curt
 */
public final class DBBrowserHandler implements RequestHandler {

    private final RequestHandler handler;

    private DBBrowserHandler(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction)
    {
        handler = CompositeRequestHandler.of(
            // handler                                                                     // for URLs of the form
            FreeFormQueryHandler.of(serversStore, managedFunction),                        // /server/select... & /server/database/select...
            DBRedirectsHandler.of(db),                                                     // action?args
            DBServersHandler.of(db,serversStore,managedFunction),                          // / , /add , /remove
            DatabaseMetaHandler.of(db,serversStore,managedFunction),                       // /meta/server/
            DatabasesHandler.of(db),                                                       // /server/
            TablesHandler.of(db,serversStore,hintsStore, managedFunction),                 // /server/databases/
            ColumnValueDistributionHandler.of(db,serversStore,hintsStore,managedFunction), // server/database/table/column
            SelectBuilderHandler.of(db,serversStore,hintsStore)                            // /server/databases/tables/...
        );
    }

    public static DBBrowserHandler of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new DBBrowserHandler(db,serversStore,hintsStore,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

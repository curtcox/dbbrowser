package com.cve.web.db;

import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.db.databases.DatabasesHandler;
import com.cve.web.*;
import com.cve.web.db.servers.DBServersHandler;
import static com.cve.util.Check.notNull;
/**
 * The {@link RequestHandler} for browsing databases.
 * @author Curt
 */
public final class DBBrowserHandler implements RequestHandler {

    final Log log;

    private final RequestHandler handler;

    private DBBrowserHandler(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction, Log log)
    {
        log.notNullArgs(db,serversStore,hintsStore,managedFunction);
        this.log = notNull(log);
        handler = CompositeRequestHandler.of(
            // handler                                                                     // for URLs of the form
            FreeFormQueryHandler.of(serversStore, managedFunction,log),                        // /server/select... & /server/database/select...
            DBRedirectsHandler.of(db,log),                                                     // action?args
            DBServersHandler.of(db,serversStore,managedFunction),                          // / , /add , /remove
            DatabaseMetaHandler.of(db,serversStore,managedFunction),                       // /meta/server/
            DatabasesHandler.of(db),                                                       // /server/
            TablesHandler.of(db,serversStore,hintsStore, managedFunction,log),                 // /server/databases/
            ColumnValueDistributionHandler.of(db,serversStore,hintsStore,managedFunction,log), // server/database/table/column
            SelectBuilderHandler.of(db,serversStore,hintsStore,managedFunction,log)            // /server/databases/tables/...
        );
    }

    public static DBBrowserHandler of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction, Log log) {
        return new DBBrowserHandler(db,serversStore,hintsStore,managedFunction,log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

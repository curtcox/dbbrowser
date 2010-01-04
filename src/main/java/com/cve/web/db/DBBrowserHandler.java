package com.cve.web.db;

import com.cve.db.dbio.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.HintsStore;
import com.cve.stores.db.ServersStore;
import com.cve.web.db.databases.DatabasesHandler;
import com.cve.web.*;
import com.cve.web.db.servers.DBServersHandler;

/**
 * The {@link RequestHandler} for browsing databases.
 * @author Curt
 */
public final class DBBrowserHandler implements RequestHandler.Factory {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final ServersStore serversStore;

    final HintsStore hintsStore;

    final ManagedFunction.Factory managedFunction;

    private DBBrowserHandler(
        DBMetaData.Factory db, ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction)
    {
        this.db = db;
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
        this.hintsStore = hintsStore;
    }

    public static DBBrowserHandler of(
        DBMetaData.Factory db, ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new DBBrowserHandler(db,serversStore,hintsStore,managedFunction);
    }

    @Override
    public RequestHandler of() {
        return CompositeRequestHandler.of(
            // handler                                  // for URLs of the form
            FreeFormQueryHandler.of(serversStore, managedFunction),      // /server/select... & /server/database/select...
            SearchRedirectsHandler.of(),                // search?find=what
            DBRedirectsHandler.of(db),                  // action?args
            DBServersHandler.of(db,serversStore,managedFunction).of(),  // / , /add , /remove
            DatabaseMetaHandler.of(db,serversStore,managedFunction),           // /meta/server/
            DatabasesHandler.of(db),              // /server/
            TablesHandler.of(db,serversStore,hintsStore, managedFunction),                 // /server/databases/
            ColumnValueDistributionHandler.of(db,serversStore,hintsStore,managedFunction), // server/database/table/column
            SelectBuilderHandler.of(db,serversStore,hintsStore)     // /server/databases/tables/...
        );
    }

}

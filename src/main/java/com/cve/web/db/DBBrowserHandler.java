package com.cve.web.db;

import com.cve.web.db.databases.DatabasesHandler;
import com.cve.web.*;
import com.cve.web.db.servers.DBServersHandler;

/**
 * The {@link RequestHandler} for browsing databases.
 * @author Curt
 */
public final class DBBrowserHandler {

    private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        new FreeFormQueryHandler(),        // /server/select... & /server/database/select...
        new SearchRedirectsHandler(),      // search?find=what
        new DBRedirectsHandler(),          // action?args
        DBServersHandler.newInstance(),    // / , /add , /remove
        DatabaseMetaHandler.newInstance(), // /meta/server/
        new DatabasesHandler(),            // /server/
        new TablesHandler(),               // /server/databases/
        new ColumnValueDistributionHandler(), // server/database/table/column
        new SelectBuilderHandler());       // /server/databases/tables/...

    public static RequestHandler newInstance() {
        return handler;
    }

}

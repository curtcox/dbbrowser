package com.cve.web.db;

import com.cve.web.*;

/**
 * The {@link RequestHandler} for browsing databases.
 * @author Curt
 */
public final class DBBrowserHandler {

    private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler                         // for URLs of the form
        new RedirectsHandler(),            // action?args
        new ServersHandler(),              // /
        DatabaseMetaHandler.newInstance(), // /server/meta/
        new DatabasesHandler(),            // /server/
        new TablesHandler(),               // /server/databases/
        new ColumnValueDistributionHandler(), // server/database/table/column
        new SelectBuilderHandler());       // /server/databases/tables/...

    public static RequestHandler newInstance() {
        return handler;
    }

}

package com.cve.web.db;

import com.cve.db.dbio.DBMetaData;
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

    private DBBrowserHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    public static DBBrowserHandler of(DBMetaData.Factory db) {
        return new DBBrowserHandler(db);
    }

    @Override
    public RequestHandler of() {
        return CompositeRequestHandler.of(
            // handler                            // for URLs of the form
            FreeFormQueryHandler.of(),             // /server/select... & /server/database/select...
            SearchRedirectsHandler.of(),           // search?find=what
            DBRedirectsHandler.of(db),             // action?args
            DBServersHandler.of(db).of(),         // / , /add , /remove
            DatabaseMetaHandler.of(db),           // /meta/server/
            DatabasesHandler.of(db),              // /server/
            TablesHandler.of(db),                 // /server/databases/
            ColumnValueDistributionHandler.of(db), // server/database/table/column
            SelectBuilderHandler.of(db)           // /server/databases/tables/...
        );
    }

}

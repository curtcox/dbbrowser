package com.cve.web.db.servers;

import com.cve.db.dbio.DBMetaData;
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

    private DBServersHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    public static DBServersHandler of(DBMetaData.Factory db) {
        return new DBServersHandler(db);
    }

    public RequestHandler of() {
        return CompositeRequestHandler.of(
            // handler                         // for URLs of the form
            ServersHandler.of(db),             // /
            new AddServerHandler(),            // /add
            new RemoveServerHandler(),         // /remove
            DatabaseMetaHandler.of(db)  // /meta/server/
        );
    }

}

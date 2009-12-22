package com.cve.web;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.dbio.LocalDBMetaDataFactory;
import com.cve.web.alt.AlternateViewHandler;
import com.cve.web.db.DBBrowserHandler;
import com.cve.web.log.LogBrowserHandler;

/**
 * A request handler for resources accessible via the local machine.
 * When replication is supported, there will be a counterpart for the
 * machine that serves the replicated data.
 * @author curt
 */
final class LocalRequestHandler {

    static RequestHandler of() {
        DBMetaData.Factory db = LocalDBMetaDataFactory.of();
        return of(db);
    }

    private static RequestHandler of(DBMetaData.Factory db) {
       return
           ErrorReportHandler.of(
                DebugHandler.of(
                    CompressedURIHandler.of(
                        CompositeRequestHandler.of(
                            CoreServerHandler.newInstance(),
                            AlternateViewHandler.of(db).of(),
                            LogBrowserHandler.newInstance(),
                            DBBrowserHandler.of(db).of()
                       )
                 )
            )
        );
    }

}

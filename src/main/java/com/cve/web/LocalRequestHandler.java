package com.cve.web;

import com.cve.db.dbio.DBMetaData;
import com.cve.web.alt.AlternateViewHandler;
import com.cve.web.db.DBBrowserHandler;
import com.cve.web.log.LogBrowserHandler;

/**
 *
 * @author curt
 */
final class LocalRequestHandler {

    static RequestHandler of() {
        throw new UnsupportedOperationException();
        //DBMetaData.Factory db = null;
        //return of(db);
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

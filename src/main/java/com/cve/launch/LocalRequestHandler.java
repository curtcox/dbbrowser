package com.cve.launch;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.dbio.LocalDBMetaDataFactory;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.HintsStore;
import com.cve.stores.db.ServersStore;
import com.cve.web.CompositeRequestHandler;
import com.cve.web.CompressedURIHandler;
import com.cve.web.CoreServerHandler;
import com.cve.web.DebugHandler;
import com.cve.web.ErrorReportHandler;
import com.cve.web.RequestHandler;
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

    final ServersStore serversStore;

    final HintsStore hintsStore;

    final DBMetaData.Factory db;

    final ManagedFunction.Factory managedFunction;

    private LocalRequestHandler(ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
        db = LocalDBMetaDataFactory.of(serversStore,managedFunction);
    }

    static LocalRequestHandler of(ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new LocalRequestHandler(serversStore, hintsStore, managedFunction);
    }

    RequestHandler of() {
        return of(db);
    }

    RequestHandler of(DBMetaData.Factory db) {
       return
           ErrorReportHandler.of(
                DebugHandler.of(
                    CompressedURIHandler.of(
                        CompositeRequestHandler.of(
                            CoreServerHandler.of(),
                            AlternateViewHandler.of(db,serversStore,hintsStore,managedFunction).of(),
                            LogBrowserHandler.newInstance(),
                            DBBrowserHandler.of(db,serversStore,hintsStore,managedFunction).of()
                       )
                 )
            )
        );
    }

}

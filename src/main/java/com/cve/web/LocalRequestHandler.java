package com.cve.web;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.dbio.LocalDBMetaDataFactory;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ServersStore;
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

    final DBMetaData.Factory db;

    final ManagedFunction.Factory managedFunction;

    private LocalRequestHandler(ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = serversStore;
        this.managedFunction = managedFunction;
        db = LocalDBMetaDataFactory.of(serversStore);
    }

    static LocalRequestHandler of(ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new LocalRequestHandler(serversStore,managedFunction);
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
                            CoreServerHandler.newInstance(),
                            AlternateViewHandler.of(db,serversStore).of(),
                            LogBrowserHandler.newInstance(),
                            DBBrowserHandler.of(db,serversStore,managedFunction).of()
                       )
                 )
            )
        );
    }

}

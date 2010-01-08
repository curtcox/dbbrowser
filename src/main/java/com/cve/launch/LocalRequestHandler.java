package com.cve.launch;

import com.cve.db.dbio.DBMetaData;
import com.cve.db.dbio.LocalDBMetaDataFactory;
import com.cve.fs.fsio.FSMetaData;
import com.cve.fs.fsio.LocalFSMetaDataFactory;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.fs.FSBrowserHandler;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.fs.FSServersStore;
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
final class LocalRequestHandler implements RequestHandler {

    private final RequestHandler handler;

    private LocalRequestHandler(
        DBServersStore dbServersStore, FSServersStore fsServersStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        final DBMetaData.Factory db = LocalDBMetaDataFactory.of(dbServersStore,managedFunction);
        final FSMetaData.Factory fs = LocalFSMetaDataFactory.of(fsServersStore,managedFunction);
        handler = ErrorReportHandler.of(
                DebugHandler.of(
                    CompressedURIHandler.of(
                        CompositeRequestHandler.of(
                            CoreServerHandler.of(),
                            AlternateViewHandler.of(db,dbServersStore,hintsStore,managedFunction),
                            LogBrowserHandler.of(),
                            FSBrowserHandler.of(fs,fsServersStore,managedFunction),
                            DBBrowserHandler.of(db,dbServersStore,hintsStore,managedFunction)
                       )
                 )
            )
        );
    }

    static LocalRequestHandler of(DBServersStore serversStore, FSServersStore fsServersStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new LocalRequestHandler(serversStore, fsServersStore, hintsStore, managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

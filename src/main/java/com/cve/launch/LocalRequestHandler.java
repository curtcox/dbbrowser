package com.cve.launch;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.io.fs.FSMetaData;
import com.cve.io.fs.LocalFSMetaDataFactory;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.fs.FSBrowserHandler;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.core.handlers.CompositeRequestHandler;
import com.cve.web.core.handlers.CompressedURIHandler;
import com.cve.web.core.handlers.CoreServerHandler;
import com.cve.web.core.handlers.DebugHandler;
import com.cve.web.core.handlers.ErrorReportHandler;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.handlers.SearchRedirectsHandler;
import com.cve.web.alt.AlternateViewHandler;
import com.cve.web.db.DBBrowserHandler;
import com.cve.web.management.ManagementHandler;

/**
 * A request handler for resources accessible via the local machine.
 * When replication is supported, there will be a counterpart for the
 * machine that serves the replicated data.
 * @author curt
 */
final class LocalRequestHandler implements RequestHandler {

    private final RequestHandler handler;

    private final Log log = Logs.of();

    private LocalRequestHandler(
        DBServersStore dbServersStore, FSServersStore fsServersStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        
        final DBMetaData.Factory db = LocalDBMetaDataFactory.of(dbServersStore,managedFunction);
        final FSMetaData.Factory fs = LocalFSMetaDataFactory.of(fsServersStore,managedFunction);
        handler = ErrorReportHandler.of(
                DebugHandler.of(
                    CompressedURIHandler.of(
                        CompositeRequestHandler.of(
                            CoreServerHandler.of(),
                            SearchRedirectsHandler.of(),              // search?find=what
                            AlternateViewHandler.of(db,dbServersStore,hintsStore,managedFunction),
                            ManagementHandler.of(),
                            FSBrowserHandler.of(fs,fsServersStore,managedFunction),
                            DBBrowserHandler.of(db,dbServersStore,hintsStore,managedFunction)
                       )
                 )
            )
        );
    }

    static LocalRequestHandler of(
        DBServersStore serversStore, FSServersStore fsServersStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        return new LocalRequestHandler(serversStore, fsServersStore, hintsStore, managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

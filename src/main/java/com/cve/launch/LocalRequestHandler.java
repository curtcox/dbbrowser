package com.cve.launch;

import com.cve.io.db.DBMetaData;
import com.cve.io.db.LocalDBMetaDataFactory;
import com.cve.io.fs.FSMetaData;
import com.cve.io.fs.LocalFSMetaDataFactory;
import com.cve.log.Log;
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
import com.cve.web.SearchRedirectsHandler;
import com.cve.web.alt.AlternateViewHandler;
import com.cve.web.db.DBBrowserHandler;
import com.cve.web.log.LogBrowserHandler;
import static com.cve.util.Check.notNull;

/**
 * A request handler for resources accessible via the local machine.
 * When replication is supported, there will be a counterpart for the
 * machine that serves the replicated data.
 * @author curt
 */
final class LocalRequestHandler implements RequestHandler {

    private final RequestHandler handler;

    private final Log log;

    private LocalRequestHandler(
        DBServersStore dbServersStore, FSServersStore fsServersStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction, Log log)
    {
        this.log = notNull(log);
        final DBMetaData.Factory db = LocalDBMetaDataFactory.of(dbServersStore,managedFunction,log);
        final FSMetaData.Factory fs = LocalFSMetaDataFactory.of(fsServersStore,managedFunction);
        handler = ErrorReportHandler.of(
                DebugHandler.of(
                    CompressedURIHandler.of(
                        CompositeRequestHandler.of(
                            CoreServerHandler.of(log),
                            SearchRedirectsHandler.of(log),              // search?find=what
                            AlternateViewHandler.of(db,dbServersStore,hintsStore,managedFunction,log),
                            LogBrowserHandler.of(log),
                            FSBrowserHandler.of(fs,fsServersStore,managedFunction,log),
                            DBBrowserHandler.of(db,dbServersStore,hintsStore,managedFunction,log)
                       ),log
                 ),log
            ),log
        );
    }

    static LocalRequestHandler of(
        DBServersStore serversStore, FSServersStore fsServersStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction, Log log)
    {
        return new LocalRequestHandler(serversStore, fsServersStore, hintsStore, managedFunction, log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

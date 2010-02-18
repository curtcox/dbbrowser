package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.core.handlers.CompositeRequestHandler;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;

/**
 *
 * @author curt
 */
final class FSServersHandler implements RequestHandler {

    private final RequestHandler handler;

    private FSServersHandler(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        this.handler = CompositeRequestHandler.of(
            // handler                         // for URLs of the form
            //ServersHandler.of(db,serversStore),             // /
            //AddServerHandler.of(serversStore),            // /add
            //new RemoveServerHandler(),         // /remove
            //DatabaseMetaHandler.of(db,serversStore,managedFunction)  // /meta/server/
        );
    }

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FSServersHandler(fs,store,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

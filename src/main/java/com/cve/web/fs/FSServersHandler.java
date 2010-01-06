package com.cve.web.fs;

import com.cve.fs.fsio.FSMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class FSServersHandler implements RequestHandler {

    private final RequestHandler handler;

    private FSServersHandler(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        this.handler = null;
    }

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FSServersHandler(fs,store,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

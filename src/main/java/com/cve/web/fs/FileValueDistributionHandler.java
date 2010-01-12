package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class FileValueDistributionHandler implements RequestHandler {

    private final RequestHandler handler;

    private FileValueDistributionHandler(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        this.handler = null;
    }

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FileValueDistributionHandler(fs,store,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }
}

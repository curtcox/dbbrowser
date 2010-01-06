package com.cve.web.fs;

import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class FreeFormPipelineHandler implements RequestHandler {

    private final RequestHandler handler;

    private FreeFormPipelineHandler(FSServersStore store, ManagedFunction.Factory managedFunction) {
        this.handler = null;
    }

    public static RequestHandler of(FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FreeFormPipelineHandler(store,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

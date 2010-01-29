package com.cve.web.fs;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.RequestHandler;

/**
 * For handling pipelines typed in from the user.
 * @author curt
 */
final class FreeFormPipelineHandler extends AbstractRequestHandler {

    final Log log = Logs.of();

    private FreeFormPipelineHandler(FSServersStore store, ManagedFunction.Factory managedFunction) {
        super();
        
    }

    public static RequestHandler of(FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FreeFormPipelineHandler(store,managedFunction);
    }

    /**
     * Do we handle this URI?
     */
    @Override
    public boolean handles(String uri) {
        log.args(uri);
        return false;
    }

    @Override
    public Model get(PageRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

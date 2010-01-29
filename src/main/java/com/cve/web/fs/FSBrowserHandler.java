package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.CompositeRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;
/**
 *
 * @author curt
 */
public final class FSBrowserHandler implements RequestHandler {

    private final RequestHandler handler;

    final Log log = Logs.of();

    private FSBrowserHandler(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        
        handler = CompositeRequestHandler.of(
            // handler                                                 // for URLs of the form
            FreeFormPipelineHandler.of(store, managedFunction),        // /server/select... & /server/database/select...
            FSRedirectsHandler.of(fs),                                 // action?args
            FSServersHandler.of(fs,store,managedFunction),             // / , /add , /remove
            FSPathHandler.of(fs),                                      // /server/
            FileValueDistributionHandler.of(fs,store,managedFunction), // server/database/table/column
            PipelineBuilderHandler.of(fs,store)                        // /server/databases/tables/...
        );
    }

    public static FSBrowserHandler of(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FSBrowserHandler(fs,store,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

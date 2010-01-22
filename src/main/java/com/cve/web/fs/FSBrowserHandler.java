package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.CompositeRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;
import static com.cve.util.Check.notNull;
/**
 *
 * @author curt
 */
public final class FSBrowserHandler implements RequestHandler {

    private final RequestHandler handler;

    final Log log;

    private FSBrowserHandler(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction, Log log) {
        this.log = notNull(log);
        handler = CompositeRequestHandler.of(
            // handler                                                 // for URLs of the form
            FreeFormPipelineHandler.of(store, managedFunction,log),        // /server/select... & /server/database/select...
            FSRedirectsHandler.of(fs,log),                                 // action?args
            FSServersHandler.of(fs,store,managedFunction),             // / , /add , /remove
            FSPathHandler.of(fs,log),                                      // /server/
            FileValueDistributionHandler.of(fs,store,managedFunction,log), // server/database/table/column
            PipelineBuilderHandler.of(fs,store,log)                        // /server/databases/tables/...
        );
    }

    public static FSBrowserHandler of(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction, Log log) {
        return new FSBrowserHandler(fs,store,managedFunction,log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

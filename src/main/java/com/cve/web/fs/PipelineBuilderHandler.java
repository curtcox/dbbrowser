package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class PipelineBuilderHandler implements RequestHandler {

    private final RequestHandler handler;

    private PipelineBuilderHandler(FSMetaData.Factory fs, FSServersStore store) {
        this.handler = null;
    }

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store) {
        return new PipelineBuilderHandler(fs,store);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

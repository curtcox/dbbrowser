package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;

/**
 *
 * @author curt
 */
final class PipelineBuilderHandler extends AbstractRequestHandler {

    private PipelineBuilderHandler(FSMetaData.Factory fs, FSServersStore store) {
        super();
    }

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store) {
        return new PipelineBuilderHandler(fs,store);
    }

    @Override
    public boolean handles(String uri) {
        return false;
    }

    @Override
    public Model get(PageRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

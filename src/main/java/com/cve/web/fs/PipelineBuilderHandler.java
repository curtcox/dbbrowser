package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class PipelineBuilderHandler extends AbstractRequestHandler {

    private PipelineBuilderHandler(FSMetaData.Factory fs, FSServersStore store, Log log) {
        super(log);
    }

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store, Log log) {
        return new PipelineBuilderHandler(fs,store,log);
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

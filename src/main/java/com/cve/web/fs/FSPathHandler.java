package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;

/**
 *
 * @author curt
 */
final class FSPathHandler extends AbstractRequestHandler  {

    private final FSMetaData.Factory fs;

    private FSPathHandler(FSMetaData.Factory fs) {
        super();
        this.fs = fs;
    }

    public static RequestHandler of(FSMetaData.Factory fs) {
        return new FSPathHandler(fs);
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

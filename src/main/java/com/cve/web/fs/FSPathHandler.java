package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class FSPathHandler extends AbstractRequestHandler  {

    private final FSMetaData.Factory fs;

    private FSPathHandler(FSMetaData.Factory fs, Log log) {
        super(log);
        this.fs = fs;
    }

    public static RequestHandler of(FSMetaData.Factory fs, Log log) {
        return new FSPathHandler(fs,log);
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

package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class FileValueDistributionHandler extends AbstractRequestHandler {

    public static RequestHandler of(FSMetaData.Factory fs, FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FileValueDistributionHandler(fs);
    }

    private final FSMetaData.Factory fs;

    private FileValueDistributionHandler(FSMetaData.Factory fs) {
        this.fs = fs;
    }

    public static RequestHandler of(FSMetaData.Factory fs) {
        return new FileValueDistributionHandler(fs);
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

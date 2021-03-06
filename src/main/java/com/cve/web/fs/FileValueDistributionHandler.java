package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.log.Log;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.RequestHandler;
import static com.cve.util.Check.*;

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
        super();
        this.fs = notNull(fs);
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

package com.cve.web.fs;

import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
public final class FSBrowserHandler implements RequestHandler {

    private FSBrowserHandler() {}

    public static FSBrowserHandler of(FSServersStore store, ManagedFunction.Factory managedFunction) {
        return new FSBrowserHandler();
    }

    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

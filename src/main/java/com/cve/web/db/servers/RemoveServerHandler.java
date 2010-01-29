package com.cve.web.db.servers;

import com.cve.log.Log;
import com.cve.stores.db.DBServersStore;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;

/**
 *
 * @author Curt
 */
final class RemoveServerHandler extends AbstractRequestHandler {

    private RemoveServerHandler(DBServersStore serversStore) {
        super();
    }

    public static RemoveServerHandler of(DBServersStore serversStore) {
        return new RemoveServerHandler(serversStore);
    }

    @Override
    public boolean handles(String uri) {
        return uri.equals("/remove");
    }

    @Override
    public RemoveServerPage get(PageRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

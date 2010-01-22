package com.cve.web.db.servers;

import com.cve.log.Log;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;

/**
 *
 * @author Curt
 */
final class RemoveServerHandler extends AbstractRequestHandler {

    private RemoveServerHandler(Log log) {
        super(log);
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

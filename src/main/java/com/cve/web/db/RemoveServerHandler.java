package com.cve.web.db;

import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Curt
 */
final class RemoveServerHandler extends AbstractRequestHandler {

    RemoveServerHandler() {}

    @Override
    public boolean handles(String uri) {
        return uri.equals("/remove");
    }

    @Override
    public PageResponse doProduce(PageRequest request) throws IOException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

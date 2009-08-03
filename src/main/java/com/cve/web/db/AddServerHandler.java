package com.cve.web.db;

import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * For adding a server.
 * @author Curt
 */
final class AddServerHandler extends AbstractRequestHandler {

    AddServerHandler() {}

    @Override
    public boolean handles(String uri) {
        return uri.equals("/add");
    }

    @Override
    public PageResponse doProduce(PageRequest request) throws IOException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

package com.cve.web.db.servers;

import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
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
    public RemoveServerPage get(PageRequest request) throws IOException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

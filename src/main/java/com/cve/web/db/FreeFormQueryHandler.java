package com.cve.web.db;

import com.cve.util.URIs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles "free-form" SQL select queries.
 * The queries are run against the server specified in the request URL.
 * If a database is specified, then it is used.
 * In other words, either
 * /server/select?q=...
 * or
 * /server/database/select?q=...
 * @author Curt
 */
final class FreeFormQueryHandler extends AbstractRequestHandler {

    FreeFormQueryHandler() {}

    /**
     * Do we handle this URI?
     */
    @Override
    public boolean handles(String uri) {
        return isFreeFormQueryRequest(uri);
    }

    @Override
    public PageResponse get(PageRequest request) throws IOException, SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Return true if URL is of the form
     * /server/db/
     */
    public static boolean isFreeFormQueryRequest(String uri) {
        if (!uri.contains("/select")) {
            return false;
        }
        int slashes = URIs.slashCount(uri);
        return slashes > 2;
    }
}

package com.cve.web.db;

import com.cve.web.*;
import com.cve.web.db.SelectBuilderAction;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.select.URIRenderer;
import com.cve.web.db.DBURICodec;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import static com.cve.util.Check.*;
import static com.cve.log.Log.args;

/**
 * For handling HTTP redirects.
 * The main source of redirects we encounter is actions tacked onto the end
 * URIs.  These actions make the page smaller (quicker to load) and more
 * readable, because they are relative URLs.
 * However, they need to be redirected to a normalized form.
 * For instance:
 * <pre>
 * /server/db/table/table.col1/filter?table.col1=active
 * </pre>
 * Can be expressed in a page via a relative URL:
 * <pre>
 * filter?table.col1=active
 * </pre>
 * When actually followed, this would be redirected to:
 * <pre>
 * /server/db/table/table.col1/table.col1=active/
 * </pre>
 * @author Curt
 */
public final class DBRedirectsHandler implements RequestHandler {

    @Override
    /**
     * Poduce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    public PageResponse produce(PageRequest request) throws IOException {
        args(request);
        notNull(request);

        String query = request.queryString;
        if (query.isEmpty()) {
            return null; // we only redirect 
        }
        try {
            String  path = request.requestURI;
            URI   dest = redirectsActionsTo(path, query);
            return PageResponse.newRedirect(dest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a path and query, produce the URI it should redirect to.
     */
    static URI redirectsActionsTo(String path, String query) throws SQLException {
        args(path,query);
        notNull(path);
        int lastSlash = path.lastIndexOf("/");
        notNegative(lastSlash);
        // the path up to the last slash
        String upToLastSlash = path.substring(0,lastSlash);
        String action = path.substring(lastSlash + 1);
        final Select select = DBURICodec.getSelect(upToLastSlash);
        Server server = DBURICodec.getServer(upToLastSlash);
        Select newSelect = SelectBuilderAction.doAction(action,select,server,query);
        Search search = DBURICodec.getSearch(upToLastSlash);
        URI dest = URIRenderer.render(newSelect,search);
        return dest;
    }


}
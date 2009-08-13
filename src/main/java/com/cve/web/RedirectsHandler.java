package com.cve.web;

import com.cve.web.db.SelectBuilderAction;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.select.URIRenderer;
import com.cve.util.URIParser;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import static com.cve.util.Check.*;
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
public final class RedirectsHandler implements RequestHandler {

    public PageResponse produce(PageRequest request) throws IOException {
        notNull(request);

        String query = request.queryString;
        if (query.isEmpty()) {
            return null;
        }
        try {
            String  path = request.requestURI;
            URI   dest = redirectsActionsTo(path, query);
            return PageResponse.newRedirect(dest);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static URI redirectsActionsTo(String path, String query) throws SQLException {
        notNull(path);
        int lastSlash = path.lastIndexOf("/");
        notNegative(lastSlash);
        String upToLastSlash = path.substring(0,lastSlash);
        String action = path.substring(lastSlash + 1);
        Select select = URIParser.getSelect(upToLastSlash);
        Server server = URIParser.getServer(upToLastSlash);
        select = SelectBuilderAction.doAction(action,select,server,query);
        URI dest = URIRenderer.render(select);
        return dest;
    }


}

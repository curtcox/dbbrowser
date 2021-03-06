package com.cve.web.db;

import com.cve.lang.URIObject;
import com.cve.model.db.Select;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.select.DBURIRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.Search;


import static com.cve.util.Check.*;
import static com.cve.util.Check.notNull;

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

    final DBMetaData.Factory db;

    final Log log = Logs.of();

    final DBURICodec codec;

    private DBRedirectsHandler(DBMetaData.Factory db) {
        this.db = notNull(db);
        
        codec = DBURICodec.of();
    }

    public static DBRedirectsHandler of(DBMetaData.Factory db) {
        return new DBRedirectsHandler(db);
    }

    /**
     * Produce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);

        String query = request.queryString;
        if (query.isEmpty()) {
            return null; // we only redirect 
        }
        String  path = request.requestURI;
        URIObject   dest = redirectsActionsTo(path, query);
        return PageResponse.newRedirect(request,dest);
    }

    /**
     * Given a path and query, produce the URIObject it should redirect to.
     */
    URIObject redirectsActionsTo(String path, String query) {
        log.args(path,query);
        int lastSlash = path.lastIndexOf("/");
        notNegative(lastSlash);
        // the path up to the last slash
        String upToLastSlash = path.substring(0,lastSlash);
        String action = path.substring(lastSlash + 1);
        final Select select = codec.getSelect(upToLastSlash);
        DBServer server = codec.getServer(upToLastSlash);
        Select newSelect = SelectBuilderAction.doAction(action,select,server,db,query);
        Search search = codec.getSearch(upToLastSlash);
        URIObject dest = DBURIRenderer.render(newSelect,search);
        return dest;
    }


}

package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.URIs;
import com.cve.web.db.DBURICodec;
import java.net.URI;

import static com.cve.util.Check.*;

/**
 * For handling HTTP search redirects.
 * Unlike other redirect handlers, this one is shared between our handling
 * of all different resource types -- database, filesystem, etc...
 * We encode the search string as the first element of the HTTP URL.  For
 * example :
 *     http://stuff+to+search+for/dbserver/dbdatabase/dbtable/
 * A redirect handler is needed, because the search string will be added in
 * the traditional
 * See also DBRedirectsHandler, FSRedirectHandler, etc...
 * @author Curt
 */
public final class SearchRedirectsHandler implements RequestHandler {

    final Log log = Logs.of();

    private SearchRedirectsHandler() {
        
    }

    public static SearchRedirectsHandler of() {
        return new SearchRedirectsHandler();
    }

    /**
     * Poduce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        notNull(request);

        String query = request.queryString;
        String  path = request.requestURI;
        if (query.isEmpty() || !path.endsWith("/search")) {
            return null;
        }
        String target = request.parameters.get(Search.FIND);
        URI dest = redirectSearchesTo(path,target);
        return PageResponse.newRedirect(request,dest);
    }

    /**
     * Construct a URI that will search for the given target on the given path.
     * For example, a request to find dogs on dog DB will be redirected as follows
     *      /+/dogdb/search?find=dogs
     *      /dogs/dogdb

     * @param path Where to search.  Note that this path should end with "/search"
     * @param target what to search for
     */
    URI redirectSearchesTo(String path, String target) {
        log.args(path,target);
        int slashes = URIs.slashCount(path);
        if (slashes==1) {
            return URIs.of("/" + target + "/");
        }
        if (slashes>1) {
            URI tail = URIs.startingAtSlash(path, 2);
            int lastSlash = tail.toString().lastIndexOf("/");
            notNegative(lastSlash);
            // the path up to the last slash
            String upToLastSlash = (lastSlash==0) ? "" : tail.toString().substring(1,lastSlash);
            String redirected = DBURICodec.encode(Search.parse(target)) + upToLastSlash;
            if (!redirected.endsWith("/")) {
                redirected = redirected + "/";
            }
            return URIs.of(redirected);
        }
        String message = path + " " + target;
        throw new IllegalArgumentException(message);
    }
}

package com.cve.web.db;

import com.cve.util.URIs;
import com.cve.web.*;
import java.io.IOException;
import java.net.URI;

import static com.cve.util.Check.*;
import static com.cve.log.Log.args;

/**
 * For handling HTTP search redirects.
 * See also DBRedirectsHandler.
 * @author Curt
 */
final class SearchRedirectsHandler implements RequestHandler {

    private SearchRedirectsHandler() {}

    public static SearchRedirectsHandler of() {
        return new SearchRedirectsHandler();
    }

    @Override
    /**
     * Poduce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    public PageResponse produce(PageRequest request) throws IOException {
        args(request);
        notNull(request);

        String query = request.queryString;
        String  path = request.requestURI;
        if (query.isEmpty() || !path.endsWith("/search")) {
            return null;
        }
        String target = request.parameters.get(Search.FIND);
        URI dest = redirectSearchesTo(path,target);
        return PageResponse.newRedirect(dest);
    }

    static URI redirectSearchesTo(String path, String target) {
        args(path,target);
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

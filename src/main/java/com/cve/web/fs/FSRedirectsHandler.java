package com.cve.web.fs;

import com.cve.model.fs.FSPipeline;
import com.cve.model.fs.FSServer;
import com.cve.io.fs.FSMetaData;
import com.cve.io.fs.pipeline.FSURIRenderer;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;
import com.cve.web.Search;
import java.net.URI;

import static com.cve.util.Check.*;
import static com.cve.log.Log.args;

/**
 *
 * @author curt
 */
final class FSRedirectsHandler implements RequestHandler {

    final FSMetaData.Factory fs;

    private FSRedirectsHandler(FSMetaData.Factory fs) {
        this.fs = fs;
    }

    public static RequestHandler of(FSMetaData.Factory fs) {
        return new FSRedirectsHandler(fs);
    }

    /**
     * Poduce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        args(request);
        notNull(request);

        String query = request.queryString;
        if (query.isEmpty()) {
            return null; // we only redirect
        }
        String  path = request.requestURI;
        URI   dest = redirectsActionsTo(path, query);
        return PageResponse.newRedirect(dest);
    }

    /**
     * Given a path and query, produce the URI it should redirect to.
     */
    URI redirectsActionsTo(String path, String query) {
        args(path,query);
        notNull(path);
        int lastSlash = path.lastIndexOf("/");
        notNegative(lastSlash);
        // the path up to the last slash
        String upToLastSlash = path.substring(0,lastSlash);
        String action = path.substring(lastSlash + 1);
        final FSPipeline select = FSURICodec.getPipeline(upToLastSlash);
        FSServer server = FSURICodec.getServer(upToLastSlash);
        FSPipeline newPipeline = PipelineBuilderAction.doAction(action,select,server,fs,query);
        Search search = FSURICodec.getSearch(upToLastSlash);
        URI dest = FSURIRenderer.render(newPipeline,search);
        return dest;
    }

}

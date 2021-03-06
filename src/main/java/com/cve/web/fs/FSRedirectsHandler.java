package com.cve.web.fs;

import com.cve.lang.URIObject;
import com.cve.model.fs.FSPipeline;
import com.cve.model.fs.FSServer;
import com.cve.io.fs.FSMetaData;
import com.cve.io.fs.pipeline.FSURIRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.Search;


import static com.cve.util.Check.*;

/**
 *
 * @author curt
 */
final class FSRedirectsHandler implements RequestHandler {

    final FSURICodec codec;

    final Log log = Logs.of();

    final FSMetaData.Factory fs;

    private FSRedirectsHandler(FSMetaData.Factory fs) {
        this.fs = notNull(fs);
        
        codec = FSURICodec.of();
    }

    public static RequestHandler of(FSMetaData.Factory fs) {
        return new FSRedirectsHandler(fs);
    }

    /**
     * Produce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        notNull(request);

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
        notNull(path);
        int lastSlash = path.lastIndexOf("/");
        notNegative(lastSlash);
        // the path up to the last slash
        String upToLastSlash = path.substring(0,lastSlash);
        String action = path.substring(lastSlash + 1);
        final FSPipeline select = codec.getPipeline(upToLastSlash);
        FSServer server = codec.getServer(upToLastSlash);
        FSPipeline newPipeline = PipelineBuilderAction.doAction(action,select,server,fs,query);
        Search search = codec.getSearch(upToLastSlash);
        URIObject dest = FSURIRenderer.render(newPipeline,search);
        return dest;
    }

}

package com.cve.web.fs;

import com.cve.model.fs.FSPipeline;
import com.cve.model.fs.FSServer;
import com.cve.io.fs.FSMetaData;
import com.cve.io.fs.pipeline.FSURIRenderer;
import com.cve.log.Log;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;
import com.cve.web.Search;
import java.net.URI;

import static com.cve.util.Check.*;

/**
 *
 * @author curt
 */
final class FSRedirectsHandler implements RequestHandler {

    final FSURICodec codec;

    final Log log;

    final FSMetaData.Factory fs;

    private FSRedirectsHandler(FSMetaData.Factory fs, Log log) {
        this.fs = notNull(fs);
        this.log = notNull(log);
        codec = FSURICodec.of(log);
    }

    public static RequestHandler of(FSMetaData.Factory fs, Log log) {
        return new FSRedirectsHandler(fs,log);
    }

    /**
     * Poduce a response with the appropriate redirect, or null if this
     * request should not be redirected.
     */
    @Override
    public PageResponse produce(PageRequest request) {
        log.notNullArgs(request);
        notNull(request);

        String query = request.queryString;
        if (query.isEmpty()) {
            return null; // we only redirect
        }
        String  path = request.requestURI;
        URI   dest = redirectsActionsTo(path, query);
        return PageResponse.newRedirect(dest,log);
    }

    /**
     * Given a path and query, produce the URI it should redirect to.
     */
    URI redirectsActionsTo(String path, String query) {
        log.notNullArgs(path,query);
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
        URI dest = FSURIRenderer.render(newPipeline,search);
        return dest;
    }

}

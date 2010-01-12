package com.cve.web.fs;

import com.cve.io.fs.FSMetaData;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class FSRedirectsHandler implements RequestHandler {

    private final RequestHandler handler;

    private FSRedirectsHandler(FSMetaData.Factory fs) {
        this.handler = null;
    }

    public static RequestHandler of(FSMetaData.Factory fs) {
        return new FSRedirectsHandler(fs);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

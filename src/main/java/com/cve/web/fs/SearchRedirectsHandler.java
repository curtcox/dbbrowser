package com.cve.web.fs;

import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.RequestHandler;

/**
 *
 * @author curt
 */
final class SearchRedirectsHandler implements RequestHandler {

    private final RequestHandler handler;

    private SearchRedirectsHandler() {
        this.handler = null;
    }

    public static RequestHandler of() {
        return new SearchRedirectsHandler();
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }

}

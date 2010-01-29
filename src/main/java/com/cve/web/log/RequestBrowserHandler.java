package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.log.ObjectRegistry.Key;

/**
 * Handle requests for an object.
 * @author curt
 */
final class RequestBrowserHandler extends AbstractRequestHandler {

    private RequestBrowserHandler() {
        super("^/request/");
    }

    static RequestBrowserHandler of() {
        return new RequestBrowserHandler();
    }

    @Override
    public ObjectModel get(PageRequest request) {
        String uri = request.requestURI;
        String idString = uri.substring(uri.lastIndexOf("/") + 1);
        Key key = ObjectRegistry.Key.parse(idString);
        Object object = ObjectRegistry.get(key);
        return new ObjectModel(object);
    }

}

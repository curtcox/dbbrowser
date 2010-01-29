package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.log.ObjectRegistry.Key;

/**
 * Handle requests for an object.
 * @author curt
 */
final class ObjectBrowserHandler extends AbstractRequestHandler {

    private ObjectBrowserHandler() {
        super("^/object/");
    }

    static ObjectBrowserHandler of() {
        return new ObjectBrowserHandler();
    }
    
    @Override
    public ObjectModel get(PageRequest request) {
        String uri = request.requestURI;
        String idString = uri.substring(uri.lastIndexOf("/") + 1);
        Key key = ObjectRegistry.Key.parse(idString);
        Object object = ObjectRegistry.get(key);
        return ObjectModel.of(object);
    }

}

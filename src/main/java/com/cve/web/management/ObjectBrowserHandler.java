package com.cve.web.management;

import com.cve.util.Strings;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.management.ObjectRegistry.Key;

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

    /**
     * Return a model for the object, if there is one.
     * Otherwise, return a model for the registry.
     */
    @Override
    public Model get(PageRequest request) {
        String uri = request.requestURI;
        String idString = uri.substring(uri.lastIndexOf("/") + 1);
        if (Strings.isEmpty(idString)) {
            return ObjectRegistryModel.of();
        }
        Key key = ObjectRegistry.Key.parse(idString);
        Object object = ObjectRegistry.get(key);
        if (object ==null) {
            return ObjectRegistryModel.of();
        }
        return ObjectModel.of(object);
    }

}

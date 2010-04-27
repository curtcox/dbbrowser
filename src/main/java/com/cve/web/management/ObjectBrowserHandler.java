package com.cve.web.management;

import com.cve.lang.RegEx;
import com.cve.util.URIs;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;

/**
 * Handle requests for an object.
 * @author curt
 */
final class ObjectBrowserHandler extends AbstractRequestHandler {

    final LogCodec codec  = LogCodec.of();
    
    private static final String PREFIX = "/object/";

    private ObjectBrowserHandler() {
        super(RegEx.of("^" + PREFIX));
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
        // /object/ -> registry
        if (uri.equals(PREFIX)) {
            return ObjectRegistryModel.of();
        }

        Object object = codec.decode(URIs.of(uri));
        return ObjectModel.of(object);
    }

}

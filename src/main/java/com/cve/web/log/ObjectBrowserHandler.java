package com.cve.web.log;

import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.log.ObjectRegistry.Key;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Handle requests for an object.
 * @author curt
 */
final class ObjectBrowserHandler extends AbstractRequestHandler {

    public ObjectBrowserHandler() {  super("^/object/"); }

    @Override
    public PageResponse get(PageRequest request) throws IOException, SQLException {
        String uri = request.requestURI;
        String idString = uri.substring(uri.lastIndexOf("/"));
        Key key = ObjectRegistry.Key.parse(idString);
        Object object = ObjectRegistry.get(key);
        return PageResponse.of(new ObjectModel(object));
    }

}

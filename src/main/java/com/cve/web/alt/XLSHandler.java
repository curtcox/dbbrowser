package com.cve.web.alt;

import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import java.io.IOException;

/**
 *
 * @author curt
 */
final class XLSHandler extends AbstractRequestHandler {

    XLSHandler() { super("^/view/csv/"); }

    public Model get(PageRequest request) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

package com.cve.web.alt;

import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import java.io.IOException;

/**
 *
 * @author curt
 */
final class PDFHandler extends AbstractRequestHandler {

    PDFHandler() { super("^/view/csv/"); }

    public PageResponse get(PageRequest request) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

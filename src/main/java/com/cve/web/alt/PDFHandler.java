package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;

/**
 *
 * @author curt
 */
final class PDFHandler extends AbstractBinaryRequestHandler {

    final Log log;

    private PDFHandler(Log log) {
        super("^/view/PDF/", ContentType.PDF,log);
        this.log = log;
    }

    public static PDFHandler of(Log log) {
        return new PDFHandler(log);
    }
    
    @Override
    public byte[] get(PageRequest request) {
        log.notNullArgs(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

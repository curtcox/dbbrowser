package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.handlers.AbstractBinaryRequestHandler;
import com.cve.web.core.ContentType;
import com.cve.web.core.PageRequest;
/**
 *
 * @author curt
 */
final class XMLHandler extends AbstractBinaryRequestHandler {

    final Log log = Logs.of();

    private XMLHandler() {
        super("^/view/XML/", ContentType.XML);
        
    }

    static XMLHandler of() {
        return new XMLHandler();
    }
    
    @Override
    public byte[] get(PageRequest request) {
        log.args(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;
/**
 *
 * @author curt
 */
final class XLSHandler extends AbstractBinaryRequestHandler {

    final Log log = Logs.of();

    private XLSHandler() {
        super("^/view/XLS/", ContentType.XLS);
        
    }

    public static XLSHandler of() {
        return new XLSHandler();
    }
    
    @Override
    public byte[] get(PageRequest request) {
        log.args(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

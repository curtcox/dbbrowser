package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;
import static com.cve.util.Check.notNull;
/**
 *
 * @author curt
 */
final class XLSHandler extends AbstractBinaryRequestHandler {

    final Log log;

    private XLSHandler(Log log) {
        super("^/view/XLS/", ContentType.XLS,log);
        this.log = notNull(log);
    }

    public static XLSHandler of(Log log) {
        return new XLSHandler(log);
    }
    
    @Override
    public byte[] get(PageRequest request) {
        log.args(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

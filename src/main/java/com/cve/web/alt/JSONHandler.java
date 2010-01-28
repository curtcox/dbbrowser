package com.cve.web.alt;

import com.cve.log.Log;
import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;
import static com.cve.util.Check.*;
/**
 *
 * @author curt
 */
final class JSONHandler extends AbstractBinaryRequestHandler {

    final Log log;

    private JSONHandler(Log log) {
        super("^/view/JSON/", ContentType.HTML,log);
        this.log = notNull(log);
    }

    static JSONHandler of(Log log) {
        return new JSONHandler(log);
    }
    
    @Override
    public byte[] get(PageRequest request) {
        log.args(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

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
final class XMLHandler extends AbstractBinaryRequestHandler {

    final Log log;

    private XMLHandler(Log log) {
        super("^/view/XML/", ContentType.XML, log);
        this.log = notNull(log);
    }

    @Override
    public byte[] get(PageRequest request) {
        log.notNullArgs(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

package com.cve.web.alt;

import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;
import static com.cve.log.Log.args;
/**
 *
 * @author curt
 */
final class JSONHandler extends AbstractBinaryRequestHandler {

    JSONHandler() { super("^/view/JSON/", ContentType.HTML); }

    @Override
    public byte[] get(PageRequest request) {
        args(request);
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

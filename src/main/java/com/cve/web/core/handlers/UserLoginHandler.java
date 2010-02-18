package com.cve.web.core.handlers;

import com.cve.log.Log;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;

/**
 *
 * @author Curt
 */
final class UserLoginHandler extends AbstractRequestHandler {

    private UserLoginHandler() {
        super();
    }

    static UserLoginHandler of() {
        return new UserLoginHandler();
    }

    @Override
    public boolean handles(String uri) {
        return uri.equals("/remove");
    }

    @Override
    public Model get(PageRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

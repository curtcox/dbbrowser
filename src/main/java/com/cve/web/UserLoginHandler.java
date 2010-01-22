package com.cve.web;

import com.cve.log.Log;

/**
 *
 * @author Curt
 */
final class UserLoginHandler extends AbstractRequestHandler {

    private UserLoginHandler(Log log) {
        super(log);
    }

    static UserLoginHandler of(Log log) {
        return new UserLoginHandler(log);
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

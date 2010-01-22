package com.cve.web;

import com.cve.log.Log;

/**
 *
 * @author Curt
 */
final class UserLogoutHandler extends AbstractRequestHandler {

    private UserLogoutHandler(Log log) {
        super(log);
    }

    static UserLogoutHandler of(Log log) {
        return new UserLogoutHandler(log);
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

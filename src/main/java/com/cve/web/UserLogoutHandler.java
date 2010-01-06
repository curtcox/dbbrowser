package com.cve.web;

/**
 *
 * @author Curt
 */
final class UserLogoutHandler extends AbstractRequestHandler {

    private UserLogoutHandler() {}

    static UserLogoutHandler of() {
        return new UserLogoutHandler();
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

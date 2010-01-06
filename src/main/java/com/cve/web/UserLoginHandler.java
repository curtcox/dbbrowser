package com.cve.web;

/**
 *
 * @author Curt
 */
final class UserLoginHandler extends AbstractRequestHandler {

    private UserLoginHandler() {}

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

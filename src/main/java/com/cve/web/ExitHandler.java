package com.cve.web;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Shutdown the server, if required.
 * @author Curt
 */
final class ExitHandler
    extends AbstractRequestHandler
{

    private static final ExitHandler HANDLER = new ExitHandler();

    static ExitHandler newInstance() {
        return HANDLER;
    }

    private ExitHandler() { super("^/exit");}

    public PageResponse doProduce(PageRequest request) throws IOException, SQLException {
        PageRequest.Method method = request.getMethod();
        if (method==PageRequest.Method.GET) {
            ExitPage question = new ExitPage();
            return PageResponse.of(question);
        }
        if (method==PageRequest.Method.POST) {
            System.exit(0);
        }
        throw new IllegalArgumentException("" + method);
    }


}

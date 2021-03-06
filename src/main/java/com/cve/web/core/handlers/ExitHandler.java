package com.cve.web.core.handlers;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.models.ExitPage;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;

/**
 * Shutdown the server, if required.
 * @author Curt
 */
final class ExitHandler
    extends AbstractFormHandler
{

    final Log log = Logs.of();

    private static final ExitHandler HANDLER = new ExitHandler();

    static ExitHandler of() {
        return HANDLER;
    }

    private ExitHandler() {
        super("^/exit");
        
    }

    @Override
    public PageResponse get(PageRequest request) {
        ExitPage question = ExitPage.of();
        return PageResponse.of(request,question);
    }

    @Override
    public PageResponse post(PageRequest request) {
        System.exit(0);
        return null;
    }


}

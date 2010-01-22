package com.cve.web;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;

/**
 * Shutdown the server, if required.
 * @author Curt
 */
final class ExitHandler
    extends AbstractFormHandler
{

    final Log log;

    private static final ExitHandler HANDLER = new ExitHandler(null);

    static ExitHandler of() {
        return HANDLER;
    }

    private ExitHandler(Log log) {
        super("^/exit");
        this.log = notNull(log);
    }

    @Override
    public PageResponse get(PageRequest request) {
        ExitPage question = new ExitPage();
        return PageResponse.of(question,log);
    }

    @Override
    public PageResponse post(PageRequest request) {
        System.exit(0);
        return null;
    }


}

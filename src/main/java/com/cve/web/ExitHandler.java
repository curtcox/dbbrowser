package com.cve.web;

/**
 * Shutdown the server, if required.
 * @author Curt
 */
final class ExitHandler
    extends AbstractFormHandler
{

    private static final ExitHandler HANDLER = new ExitHandler();

    static ExitHandler of() {
        return HANDLER;
    }

    private ExitHandler() { super("^/exit");}

    @Override
    public PageResponse get(PageRequest request) {
        ExitPage question = new ExitPage();
        return PageResponse.of(question);
    }

    @Override
    public PageResponse post(PageRequest request) {
        System.exit(0);
        return null;
    }


}

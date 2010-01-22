package com.cve.web;

import com.cve.log.Log;
import com.cve.web.log.AnnotatedStackTraceModel;

import static com.cve.util.Check.notNull;

/**
 * A wrapper for other requests to give debug-style error handling.
 */
public final class ErrorReportHandler implements RequestHandler {

    final Log log;

    /**
     * The thing that handles the requests that go OK.
     */
    private final RequestHandler handler;

    private ErrorReportHandler(RequestHandler handler, Log log) {
        this.handler = notNull(handler);
        this.log = notNull(log);
    }

    public static RequestHandler of(RequestHandler handler, Log log) {
        return new ErrorReportHandler(handler,log);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.notNullArgs(request);
        try {
            return handler.produce(request);
        } catch (Throwable t) {
            return PageResponse.of(AnnotatedStackTraceModel.throwable(t,log),log);
        }
    }

}

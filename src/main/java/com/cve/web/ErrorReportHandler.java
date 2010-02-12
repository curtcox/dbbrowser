package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.management.AnnotatedStackTraceModel;

import static com.cve.util.Check.notNull;

/**
 * A wrapper for other requests to give debug-style error handling.
 */
public final class ErrorReportHandler implements RequestHandler {

    final Log log = Logs.of();

    /**
     * The thing that handles the requests that go OK.
     */
    private final RequestHandler handler;

    private ErrorReportHandler(RequestHandler handler) {
        this.handler = notNull(handler);
        
    }

    public static RequestHandler of(RequestHandler handler) {
        return new ErrorReportHandler(handler);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        try {
            return handler.produce(request);
        } catch (Throwable t) {
            return PageResponse.of(request,AnnotatedStackTraceModel.throwable(t));
        }
    }

}

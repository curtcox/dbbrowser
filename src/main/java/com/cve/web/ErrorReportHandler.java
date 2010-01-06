package com.cve.web;

import com.cve.web.log.AnnotatedStackTraceModel;
import java.io.IOException;
import static com.cve.log.Log.args;

import static com.cve.util.Check.notNull;

/**
 * A wrapper for other requests to give debug-style error handling.
 */
public final class ErrorReportHandler implements RequestHandler {

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
        args(request);
        try {
            return handler.produce(request);
        } catch (Throwable t) {
            return PageResponse.of(AnnotatedStackTraceModel.throwable(t));
        }
    }

}

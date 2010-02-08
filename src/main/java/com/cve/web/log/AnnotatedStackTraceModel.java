package com.cve.web.log;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.*;
import com.cve.lang.AnnotatedStackTrace;
import static com.cve.util.Check.notNull;

/**
 * Wraps an AnnotatedStackTrace to produce a model.
 */
public final class AnnotatedStackTraceModel implements Model {

    static final Log log = Logs.of();

    public final AnnotatedStackTrace trace;

    private AnnotatedStackTraceModel(AnnotatedStackTrace t) {
        this.trace = notNull(t);
        
    }

    public static AnnotatedStackTraceModel trace(AnnotatedStackTrace trace) {
        return new AnnotatedStackTraceModel(trace);
    }

    public static AnnotatedStackTraceModel throwable(Throwable t) {
        return new AnnotatedStackTraceModel(log.annotatedStackTrace(t));
    }

}

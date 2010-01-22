package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.*;
import com.cve.util.AnnotatedStackTrace;
import static com.cve.util.Check.notNull;

/**
 * Wraps an AnnotatedStackTrace to produce a model.
 */
public final class AnnotatedStackTraceModel implements Model {

    public final Log log;

    public final AnnotatedStackTrace trace;

    private AnnotatedStackTraceModel(AnnotatedStackTrace t, Log log) {
        this.trace = notNull(t);
        this.log = notNull(log);
    }

    public static AnnotatedStackTraceModel trace(AnnotatedStackTrace trace, Log log) {
        return new AnnotatedStackTraceModel(trace,log);
    }

    public static AnnotatedStackTraceModel throwable(Throwable t, Log log) {
        return new AnnotatedStackTraceModel(log.annotatedStackTrace(t),log);
    }

}

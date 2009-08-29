package com.cve.web.log;

import com.cve.log.Log;
import com.cve.web.*;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Check;

/**
 * Wraps an AnnotatedStackTrace to produce a model.
 */
public final class AnnotatedStackTraceModel implements Model {

    public final AnnotatedStackTrace trace;

    private AnnotatedStackTraceModel(AnnotatedStackTrace t) {
        this.trace = Check.notNull(t);
    }

    public static AnnotatedStackTraceModel trace(AnnotatedStackTrace trace) {
        Check.notNull(trace);
        return new AnnotatedStackTraceModel(trace);
    }

    public static AnnotatedStackTraceModel throwable(Throwable t) {
        Check.notNull(t);
        return new AnnotatedStackTraceModel(Log.annotatedStackTrace(t));
    }

}

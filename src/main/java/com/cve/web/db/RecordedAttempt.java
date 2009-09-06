package com.cve.web.db;

import com.cve.util.AnnotatedStackTrace;

/**
 *
 * @author Curt
 */
public final class RecordedAttempt<V> {

    public final V value;

    public final AnnotatedStackTrace trace;

    private RecordedAttempt(V value, AnnotatedStackTrace trace) {
        this.value = value;
        this.trace = trace;
    }

    public static RecordedAttempt trace(AnnotatedStackTrace trace) {
        return new RecordedAttempt(null,trace);
    }

    public static RecordedAttempt of(Object o) {
        return new RecordedAttempt(o,AnnotatedStackTrace.NULL);
    }

}

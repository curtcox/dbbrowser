package com.cve.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

/**
 * Like a throwable, but perhaps with some method arguments.
 * @author curt
 */
public final class AnnotatedStackTrace {

    /**
     * The throwable this is generated from.
     */
    public final Throwable throwable;

    /**
     * Nested cause if there is one, or null if not.
     */
    public final AnnotatedStackTrace cause;

    /**
     * The elements in this trace
     */
    public final ImmutableList<StackTraceElement> elements;

    /**
     * Map from element -> method arguments
     */
    public final Map<StackTraceElement,Object[]> args;

    private AnnotatedStackTrace(Throwable t, Map<StackTraceElement,Object[]> args) {
        Check.notNull(t);
        Check.notNull(args);
        this.elements = ImmutableList.of(t.getStackTrace());
        Throwable because = t.getCause();
        this.cause = (because==null ) ? null : throwableArgs(because,args);
        this.throwable = t;
        this.args = ImmutableMap.copyOf(args);
    }

    public static AnnotatedStackTrace throwableArgs(Throwable t, Map<StackTraceElement,Object[]> args)
    {
        return new AnnotatedStackTrace(t,args);
    }
}

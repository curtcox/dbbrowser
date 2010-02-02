package com.cve.util;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.web.log.LogCodec;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.net.URI;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 * Like a throwable, but perhaps with some method arguments.
 * @author curt
 */
@Immutable
public final class AnnotatedStackTrace {

    final LogCodec codec = LogCodec.of();

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

    /**
     * Use in place of null
     */
    public static final AnnotatedStackTrace NULL = Null();

    private static AnnotatedStackTrace Null() {
        Throwable t = new Throwable();
        Map<StackTraceElement,Object[]> args = ImmutableMap.of();
        return new AnnotatedStackTrace(t,args);
    }

    private AnnotatedStackTrace(Throwable t, Map<StackTraceElement,Object[]> args) {
        Check.notNull(t);
        Check.notNull(args);
        this.elements = ImmutableList.of(t.getStackTrace());
        Throwable because = t.getCause();
        this.cause = (because==null ) ? null : throwableArgs(because,args);
        this.throwable = t;
        this.args = ImmutableMap.copyOf(args);
    }

    public static AnnotatedStackTrace throwableArgs(Throwable t, Map<StackTraceElement,Object[]> args) {
        return new AnnotatedStackTrace(t,args);
    }

    public Link linkTo() {
        Label text = Label.of("trace");
        URI target = codec.encode(this);
        return Link.textTarget(text, target);
    }

}

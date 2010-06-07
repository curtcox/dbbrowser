package com.cve.lang;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.web.management.LogCodec;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Like a throwable, but perhaps with some method arguments.
 * @author curt
 */
@Immutable
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
    public final ImmutableList<AnnotatedStackTraceElement> elements;

    /**
     * Map from element -> method arguments
     */
    public final ImmutableMap<AnnotatedStackTraceElement,Object[]> args;

    /**
     * Use in place of null
     */
    public static final AnnotatedStackTrace NULL = Null();

    /**
     * For mapping to and from URLs.
     */
    private static final LogCodec codec = LogCodec.of();

    private static AnnotatedStackTrace Null() {
        return new AnnotatedStackTrace();
    }

    /**
     * Special constructor, just to support Null.
     * Without this, we run into initialization circularity problems.
     */
    private AnnotatedStackTrace() {
        this.elements  = ImmutableList.of();
        this.cause     = null;
        this.throwable = new Throwable();
        this.args      = ImmutableMap.of();
    }

    /**
     * Use the factory.
     */
    private AnnotatedStackTrace(Throwable t, Map<AnnotatedStackTraceElement,Object[]> args) {
        notNull(t);
        notNull(args);
        this.elements = AnnotatedStackTraceElement.elementsOf(t);
        Throwable because = t.getCause();
        this.cause = (because==null ) ? null : throwableAnnotatedArgs(because,args);
        this.throwable = t;
        this.args = ImmutableMap.copyOf(args);
    }

    public static AnnotatedStackTrace throwableAnnotatedArgs(Throwable t, Map<AnnotatedStackTraceElement,Object[]> args) {
        return new AnnotatedStackTrace(t,args);
    }

    public static AnnotatedStackTrace throwableArgs(Throwable t, Map<StackTraceElement,Object[]> args) {
        Map<AnnotatedStackTraceElement,Object[]> annotated = Maps.newHashMap();
        for (StackTraceElement e : args.keySet()) {
            AnnotatedStackTraceElement key = AnnotatedStackTraceElement.of(e);
            annotated.put(key, args.get(e));
        }
        return new AnnotatedStackTrace(t,annotated);
    }

    public Link linkTo() {
        Label text = Label.of("trace");
        URIObject target = codec.encode(this);
        return Link.textTarget(text, target);
    }

    @Override
    public int hashCode() {
        return throwable.hashCode() ^ elements.hashCode() ^ args.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        AnnotatedStackTrace other = (AnnotatedStackTrace) o;
        return throwable.equals(other.throwable) &&
               Objects.equals(cause,other.cause) &&
               elements.equals(other.elements) &&
               args.equals(other.args)
               ;
    }


}

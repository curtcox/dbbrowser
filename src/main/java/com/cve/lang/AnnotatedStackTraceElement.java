package com.cve.lang;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * Like a stack trace element, but with real objects.
 * @author curt
 */
@Immutable
public final class AnnotatedStackTraceElement {

    /**
     * The class this element is being executed from.
     */
    public final AnnotatedClass clazz;

    /**
     * The relevant source code line number.
     */
    public final int line;

    /**
     * The source code line involved.
     */
    public final SourceCode source;

    /**
     * The constructor or method involved.
     */
    public final ExecutableElement executable;

    private AnnotatedStackTraceElement(AnnotatedClass clazz, int line, ExecutableElement method, SourceCode source) {
        this.clazz  = notNull(clazz);
        this.line   = line;
        this.executable = notNull(method);
        this.source = notNull(source);
    }

    public static AnnotatedStackTraceElement of(StackTraceElement e) {
        AnnotatedClass     clazz = AnnotatedClass.of(e);
        int                 line = e.getLineNumber();
        ExecutableElement method = executableOf(e);
        SourceCode        source = sourceOf(e);
        return new AnnotatedStackTraceElement(clazz,line,method,source);
    }

    /**
     * Return the given throwable as a list of AnnotatedStackTraceElements.
     */
    public static ImmutableList<AnnotatedStackTraceElement> elementsOf(Throwable t) {
        List list = Lists.newArrayList();
        for (StackTraceElement e : t.getStackTrace()) {
            list.add(AnnotatedStackTraceElement.of(e));
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Return the given ExecutableElement for the given, unannotated element.
     */
    private static ExecutableElement executableOf(StackTraceElement e) {
        AnnotatedClass c = AnnotatedClass.of(e);
        return c.getExecutable(e);
    }

    /**
     * Return the source line for the given, unannotated element.
     */
    private static SourceCode sourceOf(StackTraceElement e) {
        AnnotatedClass c = AnnotatedClass.of(e);
        int line = e.getLineNumber();
        if (c.source.size() > line && line > 0) {
            return c.source.get(line - 1);
        }
        return SourceCode.UNAVAILABLE;
    }

    @Override
    public int hashCode() {
        return clazz.hashCode() ^ line ^ source.hashCode() ^ executable.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        AnnotatedStackTraceElement other = (AnnotatedStackTraceElement) o;
        return clazz.equals(other.clazz) &&
                      line == other.line &&
             source.equals(other.source) &&
         executable.equals(other.executable);
    }
}

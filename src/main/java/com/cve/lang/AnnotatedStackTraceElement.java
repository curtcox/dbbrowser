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

    public final AnnotatedClass clazz;

    public final int line;

    public final ExecutableElement executable;

    private AnnotatedStackTraceElement(AnnotatedClass clazz, int line, ExecutableElement method) {
        this.clazz  = notNull(clazz);
        this.line   = line;
        this.executable = notNull(method);
    }

    public static AnnotatedStackTraceElement of(StackTraceElement e) {
        AnnotatedClass clazz = AnnotatedClass.of(e);
        int line = e.getLineNumber();
        ExecutableElement method = executableOf(e);
        return new AnnotatedStackTraceElement(clazz,line,method);
    }

    public static ImmutableList<AnnotatedStackTraceElement> elementsOf(Throwable t) {
        List list = Lists.newArrayList();
        for (StackTraceElement e : t.getStackTrace()) {
            list.add(AnnotatedStackTraceElement.of(e));
        }
        return ImmutableList.copyOf(list);
    }

    private static ExecutableElement executableOf(StackTraceElement e) {
        AnnotatedClass c = AnnotatedClass.of(e);
        return c.getExecutable(e);
    }
}

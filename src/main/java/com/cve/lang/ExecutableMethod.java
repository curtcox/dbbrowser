package com.cve.lang;

import java.lang.reflect.Method;
import static com.cve.util.Check.notNull;

/**
 * A wrapper to make a method executable.
 */
public final class ExecutableMethod extends ExecutableElement {

    public final Method inner;

    private ExecutableMethod(Method method) {
        super(method);
        inner = notNull(method);
    }

    public static ExecutableMethod of(Method method) {
        return new ExecutableMethod(method);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ inner.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        ExecutableMethod other = (ExecutableMethod) o;
        return super.equals(other) && inner.equals(other.inner);
    }
}

package com.cve.lang;

import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Constructor;

import static com.cve.util.Check.notNull;
/**
 * A wrapper to make a constructor executable.
 */
@Immutable
public final class ExecutableConstructor extends ExecutableElement {

    public final Constructor inner;

    private ExecutableConstructor(Constructor constructor) {
        super(constructor,listOf(constructor.getParameterTypes()),listOf(constructor.getExceptionTypes()));
        inner = notNull(constructor);
    }

    static ExecutableElement of(Constructor constructor) {
        return new ExecutableConstructor(constructor);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ inner.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        ExecutableConstructor other = (ExecutableConstructor) o;
        return super.equals(other) && inner.equals(other.inner);
    }

}

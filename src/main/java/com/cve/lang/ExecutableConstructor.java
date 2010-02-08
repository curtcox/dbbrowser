package com.cve.lang;

import java.lang.reflect.Constructor;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A wrapper to make a constructor executable.
 */
@Immutable
public final class ExecutableConstructor implements ExecutableElement {

    final Constructor inner;

    ExecutableConstructor(Constructor constructor) {
        inner = notNull(constructor);
    }

    @Override
    public Class getReturnType() {
        return inner.getDeclaringClass();
    }

    @Override
    public Class[] getParameterTypes() {
        return inner.getParameterTypes();
    }

    @Override
    public Class[] getExceptionTypes() {
        return inner.getExceptionTypes();
    }

    @Override
    public Class getDeclaringClass() {
        return inner.getDeclaringClass();
    }

    @Override
    public String getName() {
        return inner.getName();
    }

    @Override
    public int getModifiers() {
        return inner.getModifiers();
    }

    @Override
    public boolean isSynthetic() {
        return inner.isSynthetic();
    }
}

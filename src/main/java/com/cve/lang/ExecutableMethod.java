package com.cve.lang;

import java.lang.reflect.Method;
import static com.cve.util.Check.notNull;

/**
 * A wrapper to make a method executable.
 */
public final class ExecutableMethod implements ExecutableElement {

    final Method inner;

    ExecutableMethod(Method method) {
        inner = notNull(method);
    }

    @Override
    public Class getReturnType() {
        return inner.getReturnType();
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

    public Method getMethod() {
        return inner;
    }

    @Override
    public boolean isSynthetic() {
        return inner.isSynthetic();
    }
}

package com.cve.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Handy static methods for executables.
 * @author curt
 */
public final class Executables {

    public static ExecutableElement of(Method method) {
        return new ExecutableMethod(method);
    }

    public static ExecutableElement of(Constructor constructor) {
        return new ExecutableConstructor(constructor);
    }

}

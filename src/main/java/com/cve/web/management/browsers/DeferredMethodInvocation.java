package com.cve.web.management.browsers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static com.cve.util.Check.*;

/**
 * A DeferredMethod is a wrapper around a method so that it can be executed
 * later.  It is necessary, because we want to be able to browse any objects
 * that would be returned by a method invocation, but we don't want to actually
 * invoke the method, unless the link for that method is followed.
 */
final class DeferredMethodInvocation {

    /**
     * The object we will invoke the method on.
     */
    final Object target;

    /**
     * The method to invoke.
     */
    final Method method;

    /**
     * Use the factory.
     */
    private DeferredMethodInvocation(Object target, Method method) {
        this.target = notNull(target);
        this.method = notNull(method);
    }

    public static DeferredMethodInvocation of(Object target, Method method) {
        return new DeferredMethodInvocation(target,method);
    }

    /**
     * Invoke the method and return the result.
     */
    public Object invoke()
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        method.setAccessible(true);
        return method.invoke(target, null);
    }
}

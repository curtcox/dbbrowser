package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Check;
import com.cve.web.log.ObjectRegistry;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Our own private logging abstraction.
 */
final class SimpleLog implements Log {

    private static SimpleLog SINGLETON = new SimpleLog();


    private SimpleLog() {}

    public static SimpleLog of() {
        return SINGLETON;
    }

    /**
     * Where in the stack -> arguments
     */
    private final ThreadLocal<Map<StackTraceElement,Object[]>> stackArgs = new ThreadLocal() {
         @Override protected Map<StackTraceElement,Object[]> initialValue() {
             return Maps.newHashMap();
         }
    };

    Map<StackTraceElement,Object[]> copyStackArgs() {
        Map<StackTraceElement,Object[]> copy = Maps.newHashMap();
        Map<StackTraceElement,Object[]> thisThread = stackArgs.get();
        copy.putAll(thisThread);
        return copy;
    }

    void putArgs(StackTraceElement element, Object[] args) {
        stackArgs.get().put(element, args);
    }

    /**
     * Return an annotated stack trace for here.
     */
    @Override
    public AnnotatedStackTrace annotatedStackTrace() {
        return AnnotatedStackTrace.throwableArgs(new Throwable(),copyStackArgs());
    }

    /**
     * Return an annotated stack trace for the given throwable.
     */
    @Override
    public AnnotatedStackTrace annotatedStackTrace(Throwable t) {
        return AnnotatedStackTrace.throwableArgs(t,copyStackArgs());
    }

    /**
     * Note the given arguments for the method being executed.
     */
    @Override
    public void possiblyNullArgs(Object... objects) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        putArgs(element,objects);
        for (Object o : objects) {
            ObjectRegistry.put(o);
        }
        // System.out.println(element + " " + Arrays.asList(objects));
    }

    /**
     * Note the given arguments for the method being executed.
     * Throw an exception if any are null.
     */
    @Override
    public void args(Object... objects) {
        for (Object o : objects) {
            Check.notNull(o);
        }
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        putArgs(element,objects);
        for (Object o : objects) {
            ObjectRegistry.put(o);
        }
        // System.out.println(element + " " + Arrays.asList(objects));
    }

    private String caller() {
        return "";
    }

    @Override
    public void debug(String message) {
        //System.out.println(clazz + ":" + message);
    }

    @Override
    public void info(String message) {
        System.out.println(caller() + ":" + message);
    }

    @Override
    public void warn(String message) {
        System.out.println(caller() + ":" + message);
    }

    @Override
    public void warn(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void severe(String message) {
        System.out.println(caller() + ":" + message);
    }

}

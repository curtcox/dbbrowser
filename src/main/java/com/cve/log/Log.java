package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Check;
import com.cve.util.SimpleCache;
import com.cve.web.log.ObjectRegistry;

/**
 * Our own private logging abstraction.
 */
public final class Log {

    private final Class clazz;

    private static final SimpleCache<StackTraceElement,Object[]> args = SimpleCache.of();

    private Log(Class c) {
        this.clazz = Check.notNull(c);
    }

    public static Log of(Class c) {
        return new Log(c);
    }

    /**
     * Return an annotated stack trace for here.
     */
    public static AnnotatedStackTrace annotatedStackTrace() {
        return AnnotatedStackTrace.throwableArgs(new Throwable(),args.copy());
    }

    /**
     * Return an annotated stack trace for the given throwable.
     */
    public static AnnotatedStackTrace annotatedStackTrace(Throwable t) {
        return AnnotatedStackTrace.throwableArgs(t,args.copy());
    }

    /**
     * Note the given arguments for the method being executed.
     */
    public static void args(Object... objects) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        args.put(element,objects);
        for (Object o : objects) {
            ObjectRegistry.put(o);
        }
        // System.out.println(element + " " + Arrays.asList(objects));
    }

    /**
     * Note the given arguments for the method being executed.
     * Throw an exception if any are null.
     */
    public static void notNullArgs(Object... objects) {
        for (Object o : objects) {
            Check.notNull(o);
        }
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        args.put(element,objects);
        for (Object o : objects) {
            ObjectRegistry.put(o);
        }
        // System.out.println(element + " " + Arrays.asList(objects));
    }

    public void debug(String message) {
        //System.out.println(clazz + ":" + message);
    }

    public void info(String message) {
        System.out.println(clazz + ":" + message);
    }

    public void warn(String message) {
        System.out.println(clazz + ":" + message);
    }

    public void warn(Throwable t) {
        t.printStackTrace();
    }

    public void severe(String message) {
        System.out.println(clazz + ":" + message);
    }

}

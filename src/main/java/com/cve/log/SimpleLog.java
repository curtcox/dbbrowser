package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Check;
import com.cve.util.SimpleCache;
import com.cve.web.log.ObjectRegistry;

/**
 * Our own private logging abstraction.
 */
public final class SimpleLog implements Log {


    private final Class clazz;

    private static final SimpleCache<StackTraceElement,Object[]> args = SimpleCache.of();

    private SimpleLog(Class c) {
        this.clazz = Check.notNull(c);
    }

    public static SimpleLog of(Class c) {
        return new SimpleLog(c);
    }

    public static Log of() {
        return of(Log.class);
    }

    /**
     * Return an annotated stack trace for here.
     */
    @Override
    public AnnotatedStackTrace annotatedStackTrace() {
        return AnnotatedStackTrace.throwableArgs(new Throwable(),args.copy());
    }

    /**
     * Return an annotated stack trace for the given throwable.
     */
    @Override
    public AnnotatedStackTrace annotatedStackTrace(Throwable t) {
        return AnnotatedStackTrace.throwableArgs(t,args.copy());
    }

    /**
     * Note the given arguments for the method being executed.
     */
    @Override
    public void args(Object... objects) {
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
    @Override
    public void notNullArgs(Object... objects) {
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

    @Override
    public void debug(String message) {
        //System.out.println(clazz + ":" + message);
    }

    @Override
    public void info(String message) {
        System.out.println(clazz + ":" + message);
    }

    @Override
    public void warn(String message) {
        System.out.println(clazz + ":" + message);
    }

    @Override
    public void warn(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void severe(String message) {
        System.out.println(clazz + ":" + message);
    }

}

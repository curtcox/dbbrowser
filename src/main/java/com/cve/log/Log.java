package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Check;
import com.cve.web.log.ObjectRegistry;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Our own private logging abstraction.
 */
public final class Log {

    private final Class clazz;

    private static final Map<StackTraceElement,Object[]> map = new ConcurrentHashMap();

    private Log(Class c) {
        this.clazz = Check.notNull(c);
    }

    public static Log of(Class c) {
        return new Log(c);
    }

    public static AnnotatedStackTrace annotatedStackTrace(Throwable t) {
        return AnnotatedStackTrace.throwableArgs(t,map);
    }

    public static void args(Object... objects) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        map.put(element, objects);
        for (Object o : objects) {
            ObjectRegistry.put(o);
        }
        // System.out.println(element + " " + Arrays.asList(objects));
    }

    public void info(String message) {
        System.out.println(message);
    }

    public void warn(String message) {
        System.out.println(message);
    }

    public void warn(Throwable t) {
        t.printStackTrace();
    }

    public void severe(String message) {
        System.out.println(message);
    }

}

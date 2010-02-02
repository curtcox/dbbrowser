package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Check;
import com.cve.web.PageRequest;
import com.cve.web.log.ObjectRegistry;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

/**
 * Our own private logging abstraction.
 */
final class SimpleLog implements Log {

    private static int OFFSET_FOR_CALLER = 4;
    
    private static Log SINGLETON = LogWrapper.of(new SimpleLog());


    private SimpleLog() {}

    public static Log of() {
        return SINGLETON;
    }

    /**
     * Where in the stack -> arguments.
     * This is wrapped in a ThreadLocal, so different threads don't overlay
     * each others info.
     */
    private final ThreadLocal<Map<StackTraceElement,Object[]>> stackArgs = new ThreadLocal() {
         @Override protected Map<StackTraceElement,Object[]> initialValue() {
             return Maps.newHashMap();
         }
    };

    /**
     * Make a copy of the arg stack as it exists for this thread, now.
     * @return
     */
    private Map<StackTraceElement,Object[]> copyStackArgs() {
        Map<StackTraceElement,Object[]> copy = Maps.newHashMap();
        Map<StackTraceElement,Object[]> thisThread = stackArgs.get();
        copy.putAll(thisThread);
        return copy;
    }

    /**
     * Note that the element had the given args.
     */
    private void note(StackTraceElement element, Object[] args) {
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
        StackTraceElement element = elements[OFFSET_FOR_CALLER];
        note(element,objects);
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
        StackTraceElement element = elements[OFFSET_FOR_CALLER];
        note(element,objects);
        for (Object o : objects) {
            ObjectRegistry.put(o);
        }
        // System.out.println(element + " " + Arrays.asList(objects));
    }


    @Override
    public void debug(Object... args) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement element = elements[3];
        note(element,args);
        for (Object o : args) {
            ObjectRegistry.put(o);
        }
        LogLevel            level = LogLevel.DEBUG;
        PageRequest.ID         id = PageRequest.ID.of();
        AnnotatedStackTrace trace = annotatedStackTrace();
        Class              logger = caller(trace);
        String            message = logger.getName();
        ObjectRegistry.put(LogEntry.of(level,id,trace,logger,message,args));
        //System.out.println(clazz + ":" + message);
    }

    @Override
    public void info(String message) {
        LogLevel            level = LogLevel.DEBUG;
        PageRequest.ID         id = PageRequest.ID.of();
        AnnotatedStackTrace trace = annotatedStackTrace();
        Class               logger = caller(trace);
        ObjectRegistry.put(LogEntry.of(level,id,trace,logger,message));
        print(logger,message);
    }

    @Override
    public void warn(String message) {
        LogLevel            level = LogLevel.DEBUG;
        PageRequest.ID         id = PageRequest.ID.of();
        AnnotatedStackTrace trace = annotatedStackTrace();
        Class               logger = caller(trace);
        ObjectRegistry.put(LogEntry.of(level,id,trace,logger,message));
        print(logger,message);
    }

    @Override
    public void warn(Throwable t) {
        LogLevel            level = LogLevel.DEBUG;
        PageRequest.ID         id = PageRequest.ID.of();
        AnnotatedStackTrace trace = annotatedStackTrace(t);
        String            message = t.getMessage();
        Class               logger = caller(trace);
        ObjectRegistry.put(LogEntry.of(level,id,trace,logger,message));
        t.printStackTrace();
    }

    @Override
    public void severe(String message) {
        LogLevel            level = LogLevel.DEBUG;
        PageRequest.ID         id = PageRequest.ID.of();
        AnnotatedStackTrace trace = annotatedStackTrace();
        Class               logger = caller(trace);
        ObjectRegistry.put(LogEntry.of(level,id,trace,logger,message));
        print(logger,message);
    }

    static ImmutableSet<String> IGNORE_AS_CALLERS = callersToIgnore();

    static ImmutableSet<String> callersToIgnore() {
        Set<String> ignore = Sets.newHashSet();
        ignore.add(SimpleLog.class.getName());
        ignore.add(LogWrapper.class.getName());
        return ImmutableSet.copyOf(ignore);
    }

    Class caller(AnnotatedStackTrace t) {
        for (StackTraceElement element : t.elements) {
            String name = element.getClassName();
            if (!IGNORE_AS_CALLERS.contains(name)) {
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    return e.getClass();
                }
            }
        }
        return null;
    }

    void print(Class logger, String message) {
        System.out.println(logger.getSimpleName() + ":" + message);
    }
}

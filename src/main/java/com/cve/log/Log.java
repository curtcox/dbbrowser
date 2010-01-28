package com.cve.log;

import com.cve.util.AnnotatedStackTrace;

/**
 * Our own private logging abstraction.
 */
public interface Log {


    /**
     * Return an annotated stack trace for here.
     */
    AnnotatedStackTrace annotatedStackTrace();

    /**
     * Return an annotated stack trace for the given throwable.
     */
    AnnotatedStackTrace annotatedStackTrace(Throwable t);


    void possiblyNullArgs(Object... objects);

    /**
     * Note the given arguments for the method being executed.
     * Throw an exception if any are null.
     */
    void args(Object... objects);

    void debug(String message);

    void info(String message);

    void warn(String message);

    void warn(Throwable t);

    void severe(String message);

}

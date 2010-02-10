package com.cve.log;

import com.cve.lang.AnnotatedStackTrace;

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


    /**
     * Note the given arguments for the method being executed.
     * Don't throw an exception if any are null.
     */
    void possiblyNullArgs(Object... objects);

    /**
     * Note the given arguments for the method being executed.
     * Throw an exception if any are null.
     */
    void args(Object... objects);

    /**
     * Log this at the debug level.
     */
    void debug(Object... args);

    /**
     * Log this at the info level.
     */
    void info(String message);

    /**
     * Log this at the warning level.
     */
    void warn(String message);

    /**
     * Log this at the warning level.
     */
    void warn(Throwable t);

    /**
     * Log this at the severe level.
     */
    void severe(String message);

}

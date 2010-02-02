package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
final class LogWrapper implements Log {

    final Log log;

    final Object lock = new Object();

    private LogWrapper(Log log) {
        this.log = notNull(log);
    }

    public static Log of(Log log) {
        return new LogWrapper(log);
    }

    @Override
    public AnnotatedStackTrace annotatedStackTrace() {
        synchronized (lock) {
            return log.annotatedStackTrace();
        }
    }

    @Override
    public AnnotatedStackTrace annotatedStackTrace(Throwable t) {
        synchronized (lock) {
            return log.annotatedStackTrace(t);
        }
    }

    @Override
    public void possiblyNullArgs(Object... objects) {
        synchronized (lock) {
            log.possiblyNullArgs(objects);
        }
    }

    @Override
    public void args(Object... objects) {
        synchronized (lock) {
            log.args(objects);
        }
    }

    @Override
    public void debug(Object... args) {
        synchronized (lock) {
            log.debug(args);
        }
    }

    @Override
    public void info(String message) {
        synchronized (lock) {
            log.info(message);
        }
    }

    @Override
    public void warn(String message) {
        synchronized (lock) {
            log.warn(message);
        }
    }

    @Override
    public void warn(Throwable t) {
        synchronized (lock) {
            log.warn(t);
        }
    }

    @Override
    public void severe(String message) {
        synchronized (lock) {
            log.severe(message);
        }
    }

}

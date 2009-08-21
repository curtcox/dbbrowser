package com.cve.log;

import com.cve.util.Check;

/**
 * Our own private logging abstraction.
 */
public final class Log {

    private final Class clazz;

    private Log(Class c) {
        this.clazz = Check.notNull(c);
    }

    public static Log of(Class c) {
        return new Log(c);
    }

    public void debug(String message) {
        System.out.println(message);
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

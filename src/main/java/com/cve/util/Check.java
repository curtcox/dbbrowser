package com.cve.util;

import java.awt.EventQueue;

/**
 * For checking method praramters.
 */
public final class Check {

    public static <T> T notNull(T t) {
        if (t==null) {
            throw new NullPointerException();
        }
        return t;
    }

    public static <T> T notNull(T t, String message) {
        if (t==null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    public static int notNegative(int i) {
        if (i<0) {
            String message = i + " < 0";
            throw new IllegalArgumentException(message);
        }
        return i;
    }

    public static void isEDT() {
        if (!EventQueue.isDispatchThread()) {
            String message = "This should only be done from the EDT.";
            throw new IllegalThreadStateException(message);
        }
    }

}

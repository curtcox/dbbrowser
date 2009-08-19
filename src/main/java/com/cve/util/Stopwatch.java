package com.cve.util;

/**
 * For determining where the time goes.
 * @author curt
 */
public final class Stopwatch {

    private final String message;
    private final Throwable here = new Throwable();
    private final long start = System.currentTimeMillis();

    private static final long THRESHHOLD = 100;

    private Stopwatch(String message) {
        this.message = message;
    }

    public static Stopwatch start(Object object) {
        return new Stopwatch(object.toString());
    }

    public void stop() {
        long end = System.currentTimeMillis();
        long duration = end - start;
        if (duration > THRESHHOLD) {
            System.err.println(message);
            System.err.println("Duration = " + duration);
            here.printStackTrace();
        }
    }
}

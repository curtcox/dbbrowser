package com.cve.util;

/**
 * For determining where the time goes.
 * @author curt
 */
public final class Stopwatch {


    /**
     * Message to display if threshhold is exceeded.
     */
    private final String message;

    /**
     * Where we were created.
     */
    private final Throwable here = new Throwable();

    /**
     * When we were created.
     */
    private final long start = System.currentTimeMillis();

    /**
     * How long is too long?
     */
    private static final long THRESHHOLD = 100;

    private Stopwatch(String message) {
        this.message = message;
    }

    /**
     * Start a new stopwatch.
     * @return
     */
    public static Stopwatch start() {
        return new Stopwatch("");
    }

    public static Stopwatch start(Object object) {
        return new Stopwatch(object.toString());
    }

    /**
     * Stop the stopwatch a complain if it took too long.
     */
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

/*
 * File:    Timer.java
 * Authors: Charlie Beck, Phoebe Hughes, Tiffany Lam, Jenny Lin
 * Date:    April 10, 2017
 * Project: 3
 * Copied from PrimeFinder
 */

/**
 * Timer util class
 */
public class Timer {

    /** The start time. */
    private static long startTime;

    /** The end time. */
    private static long endTime;

    /**
     * Starts the timer.
     */
    public static void start() {
        startTime = System.nanoTime();
    }

    /**
     * Stops the timer.
     */
    public static void stop() {
        endTime = System.nanoTime();
    }

    /**
     * Calculates the runtime.
     * @return the milliseconds between the last call of start and stop
     */
    public static long getRuntime() {
        return (endTime - startTime) / 1000000;
    }
}
package com.cve.lang;

import com.cve.util.Check;

/**
 * Handy methods for dealing with longs.
 * @author curt
 */
public final class Longs {

    //                                     12345678.2345678
    private static final long LO_ONES = 0x00000000ffffffffL;

    /**
     * Return the given long as a pair of ints.
     */
    public static int[] toInts(final long value) {
        int lo = (int) value;
        //int hi = (int) ((value >> 32L) & LO_ONES);
        int hi = (int) (value >>> 32L); // & LO_ONES);
        return new int[] {lo,hi };
    }

    /**
     * Return the given ints as a long.
     */
    public static long fromInts(int a, int b) {
        // Mask off the high bits to circumvent sign extension.
        long lo = a & LO_ONES;
        long hi = b & LO_ONES;
        return lo | (hi << 32L);
    }

    /**
     * Return the given ints as a long.
     */
    public static long fromInts(int[] a) {
        Check.notNull(a);
        if (a.length!=2) {
            throw new IllegalArgumentException();
        }
        return fromInts(a[0],a[1]);
    }

}

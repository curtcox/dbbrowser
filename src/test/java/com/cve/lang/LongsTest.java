package com.cve.lang;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class LongsTest {

    static int MAX_INT = Integer.MAX_VALUE;
    static int MIN_INT = Integer.MIN_VALUE;

    @Test public void             _m2() { test(-2L,-2,-1); }
    @Test public void             _m1() { test(-1L,-1,-1); }
    @Test public void              _0() { test(0L,0,0); }
    @Test public void              _1() { test(1L,1,0); }
    @Test public void              _2() { test(2L,2,0); }
    @Test public void              _3() { test(3L,3,0); }
    @Test public void           _1_s2() { test(1L << 2,  4       ,0 ); }
    @Test public void          _1_s30() { test(1L << 30, 1 << 30 ,0 ); }
    @Test public void          _1_s31() { test(1L << 31, 1 << 31 ,0 ); }
    @Test public void          _1_s32() { test(1L << 32, 0       ,1 ); }
    @Test public void          _1_s34() { test(1L << 34, 0       ,1 << 2); }
    @Test public void          _1_s63() { test(1L << 63, 0       ,1 << 31); }
    @Test public void   int_max_value() { test(MAX_INT, MAX_INT,0); }
    @Test public void   int_min_value() { test(MIN_INT, MIN_INT,-1); }
    @Test public void  long_max_value() { test(Long.MAX_VALUE,-1,MAX_INT); }
    @Test public void  long_min_value() { test(Long.MIN_VALUE,0,MIN_INT); }

    /**
     * Ensure the given long can be converted to and from a pair of ints.
     * @param value the long value
     * @param lo the low int
     * @param hi the high int
     */
    void test(long value, int lo, int hi) {
        int[] a = Longs.toInts(value);
        equals(lo,a[0]);
        equals(hi,a[1]);
        long same = Longs.fromInts(a);
        equals(value, same);
    }

    void equals(int a, int b) {
        String message = Integer.toBinaryString(a) + "!=" + Integer.toBinaryString(b);
        assertEquals(message,a,b);
    }

    void equals(long a, long b) {
        String message = Long.toBinaryString(a) + "!=" + Long.toBinaryString(b);
        assertEquals(message,a,b);
    }
}

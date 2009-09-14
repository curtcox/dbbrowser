package com.cve.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class URLCodecTest {

    @Test public void        foo() { test("foo"); }
    @Test public void      space() { test(" "); }
    @Test public void underscore() { test("_"); }
    @Test public void       plus() { test("+"); }
    @Test public void  percent2b() { test("%2B"); }

    void test(String s) {
        String encoded = URLCodec.encode(s);
        assertFalse(encoded.contains("+"));
        URIs.of(encoded);
        assertEquals(s, URLCodec.decode(encoded));
    }
}

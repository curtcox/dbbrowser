package com.cve.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class URIsTest {

    public URIsTest() {}


    @Test(expected=IllegalArgumentException.class)
    public void spacesNotAllowed() {
        URIs.of("            ");
    }

    @Test
    public void googleOK() {
        URIs.of("http://www.google.com/");
    }

    @Test
    public void slashCount() {
        assertEquals(0,URIs.slashCount(""));
        assertEquals(0,URIs.slashCount("?"));
        assertEquals(0,URIs.slashCount("1?1"));
        assertEquals(1,URIs.slashCount("/"));
        assertEquals(1,URIs.slashCount("?/?"));
        assertEquals(2,URIs.slashCount("//"));
        assertEquals(2,URIs.slashCount("?//?"));
        assertEquals(2,URIs.slashCount("?/?/?"));
        assertEquals(2,URIs.slashCount("/?/"));
        assertEquals(3,URIs.slashCount("///"));
        assertEquals(3,URIs.slashCount("?///?"));
        assertEquals(3,URIs.slashCount("/?/?/"));
    }
}

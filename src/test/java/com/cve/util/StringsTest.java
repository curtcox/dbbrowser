package com.cve.util;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class StringsTest {

    @Test
    public void hashOfString() {
        String s = "";
        assertEquals(s.hashCode(),Strings.hashCode(s));
    }

    @Test
    public void hashOfNullIsZero() {
        assertEquals(0,Strings.hashCode(null));
    }

    @Test
    public void emptyStringsAreEqual() {
        String s = "";
        assertTrue(Strings.equals(s, s.toString()));
    }

    @Test
    public void nullStringsAreEqual() {
        assertTrue(Strings.equals(null, null));
    }

    @Test
    public void nonEmptyEqualStringsAreEqual() {
        String s = "foo";
        assertTrue(Strings.equals(s, s.toString()));
    }

    @Test
    public void nonEqualStringsAreNotEqual() {
        assertFalse(Strings.equals("foo", "bar"));
    }

    @Test
    public void nullIsNotEqualNonNull() {
        assertFalse(Strings.equals(null, "foo"));
    }

}

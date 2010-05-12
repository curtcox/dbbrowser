package com.cve.model.test;


/**
 * This wraps JUnit assertions, so our tests can be run under either JUnit, or
 * our own framework.
 * @author curt
 */
public final class Assert {

    public static void notNull(Object o) {
        org.junit.Assert.assertNotNull(o);
    }

    public static void equals(Object a, Object b) {
        org.junit.Assert.assertEquals(a,b);
    }

    public static void that(boolean condition) {
        org.junit.Assert.assertTrue(condition);
    }
}

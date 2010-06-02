package com.cve.model.test;


/**
 * This wraps JUnit assertions, so our tests can be run under either JUnit, or
 * our own framework.
 * @author curt
 */
public final class Assert {

    public static Assertion notNull(Object o) {
        org.junit.Assert.assertNotNull(o);
        return Assertion.ofNotNull(o);
    }

    public static Assertion equals(Object a, Object b) {
        org.junit.Assert.assertEquals(a,b);
        return Assertion.ofEquality(a,b);
    }

    public static Assertion that(boolean condition) {
        org.junit.Assert.assertTrue(condition);
        return Assertion.ofTruth(condition);
    }

     public static Assertion failure() {
        org.junit.Assert.fail();
        return Assertion.ofFailure();
    }

}

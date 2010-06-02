package com.cve.model.test;

import com.cve.util.Check;
import javax.annotation.concurrent.Immutable;

/**
 * Something that is being asserted.
 * An Assertion is usually created in a test using a static method of Assert.
 * @author curt
 */
@Immutable
public final class Assertion {

    /**
     * The value the assertion expects.
     */
    final Object expected;

    /**
     * The actual value encountered.
     */
    final Object actual;

    /*
     * The relationship between expected and actual.
     */
    final Relationship relationship;

    /**
     * Use in place of null.
     */
    static final Object NULL = new Object();

    private Assertion(Object expected, Object actual, Relationship relationship) {
        this.expected     = Check.notNull(expected);
        this.actual       = Check.notNull(actual);
        this.relationship = Check.notNull(relationship);
    }

    static Assertion ofNotNull(Object actual) {
        return new Assertion(NULL,actual,Relationship.NOT_NULL);
    }

    static Assertion ofEquality(Object expected, Object actual) {
        return new Assertion(expected,actual,Relationship.EQUALS);
    }

    static Assertion ofTruth(boolean condition) {
        return new Assertion(true,condition,Relationship.TRUE);
    }

    static Assertion ofFailure() {
        return new Assertion(NULL,NULL,Relationship.NONE);
    }

    static enum Result {
        PASS,
        FAIL,
        ERROR,
        ;
    }

    static enum Relationship {
        EQUALS,
        NOT_EQUALS,
        NULL,
        NOT_NULL,
        TRUE,
        FALSE,
        NONE
        ;
    }
}

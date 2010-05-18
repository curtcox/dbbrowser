package com.cve.model.test;

import com.cve.lang.AnnotatedPackage;
import com.cve.util.Check;

/**
 * The thing that is being tested by a test.
 * Method, class, package, etc...
 * @author curt
 */
public final class TestTarget {

    final Object target;

    private TestTarget(Object target) {
        this.target = Check.notNull(target);
    }

    static TestTarget of(AnnotatedPackage root) {
        return new TestTarget(root);
    }

}

package com.cve.model.test;

import com.cve.lang.AnnotatedCallTree;
import com.cve.lang.AnnotatedStackTraceElement;
import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 * The results of running a single test method.
 * @author curt
 */
@Immutable
public final class AnnotatedTest {

    /**
     * What happened when the method was run.
     */
    final AnnotatedCallTree calls;

    /**
     * How the test was invoked.
     */
    final AnnotatedStackTraceElement invoked;

    /**
     * What happened for each assertion?
     */
    final ImmutableMap<Assertion,Assertion.Result> results;

    private AnnotatedTest(AnnotatedCallTree calls, AnnotatedStackTraceElement invoked, Map<Assertion,Assertion.Result> results) {
        this.calls   = Check.notNull(calls);
        this.invoked = Check.notNull(invoked);
        this.results = ImmutableMap.copyOf(results);
    }
}

package com.cve.model.test;

import com.cve.lang.AnnotatedCallTree;
import com.cve.lang.AnnotatedPackage;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 * The results of running a test.
 * <p>
 * Tests are grouped hierarchically.
 * Generally either tests or results will be empty.
 * This is because a test will either have a list of assertions, or a
 * set of sub tests, but not both.
 * @author curt
 */
@Immutable
public final class AnnotatedTest {

    /*
     * The thing this test tests.  Method, class, package, etc...
     */
    final TestTarget target;

    /**
     * What happened when the test was run.
     */
    final AnnotatedCallTree calls;

    /**
     * How the test was invoked.
     */
    final AnnotatedStackTrace invoked;

    /**
     * What tests, if any, are part of this test?
     * Generally either tests or results will be empty.
     */
    final ImmutableList<AnnotatedTest> tests;

    /**
     * What happened for each assertion?
     * Generally either tests or results will be empty.
     */
    final ImmutableMap<Assertion,Assertion.Result> results;

    private AnnotatedTest(
        TestTarget target,
        List<AnnotatedTest> tests,
        AnnotatedCallTree calls, AnnotatedStackTrace invoked,
        Map<Assertion,Assertion.Result> results) {
        this.target  = Check.notNull(target);
        this.tests   = ImmutableList.copyOf(tests);
        this.calls   = Check.notNull(calls);
        this.invoked = Check.notNull(invoked);
        this.results = ImmutableMap.copyOf(results);
    }

    public static AnnotatedTest of(AnnotatedPackage aPackage) {
        TestTarget target = TestTarget.of(aPackage);
        List<AnnotatedTest> tests = forPackages(aPackage.packages);
        Log log = Logs.of();
        AnnotatedCallTree calls = log.annotatedCallTree();
        AnnotatedStackTrace invoked = log.annotatedStackTrace();
        Map<Assertion,Assertion.Result> results = ImmutableMap.of();
        return new AnnotatedTest(target,tests,calls,invoked,results);
    }

    private static List<AnnotatedTest> forPackages(ImmutableList<AnnotatedPackage> packages) {
        List<AnnotatedTest> tests = Lists.newArrayList();
        return tests;
    }

    /**
     * Produce a test page for the root package and display it.
     */
    public static void main(String[] args) {
        AnnotatedTest test = AnnotatedTest.of(AnnotatedPackage.getRoot());
    }
}

package com.cve.lang;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class AnnotatedStackTraceElementTest {

    @Test
    public void twoElementsFromTheSameElementAreEqual() {
        StackTraceElement element = new Throwable().getStackTrace()[0];
        assertEquals(AnnotatedStackTraceElement.of(element),AnnotatedStackTraceElement.of(element));
    }
}

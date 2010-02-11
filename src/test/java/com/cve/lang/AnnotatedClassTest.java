package com.cve.lang;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class AnnotatedClassTest {

    @Test
    public void twoClassesFromTheSameClassAreEqual() {
        Class c = AnnotatedClassTest.class;
        assertEquals(AnnotatedClass.of(c),AnnotatedClass.of(c));
    }
}

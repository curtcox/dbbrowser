package com.cve.lang;

import java.lang.reflect.Method;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ExecutableMethodTest {

    @Test
    public void twoMethodsFromTheSameMethodAreEqual() {
        Method method = Method.class.getDeclaredMethods()[0];
        assertEquals(ExecutableMethod.of(method),ExecutableMethod.of(method));
    }
}

package com.cve.lang;

import java.lang.reflect.Constructor;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ExecutableConstructorTest {

    @Test
    public void twoConstructorsFromTheSameConstructorAreEqual() {
        Constructor method = Constructor.class.getDeclaredConstructors()[0];
        assertEquals(ExecutableConstructor.of(method),ExecutableConstructor.of(method));
    }
}

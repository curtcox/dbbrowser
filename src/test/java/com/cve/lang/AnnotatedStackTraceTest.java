package com.cve.lang;

import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class AnnotatedStackTraceTest {

    @Test
    public void traceRetainsArgsFromMap() {
        Throwable t = new Throwable();
        Map<StackTraceElement,Object[]> args = Maps.newHashMap();
        for (StackTraceElement element : t.getStackTrace()) {
            args.put(element, new Object[] { element });
        }
        AnnotatedStackTrace trace = AnnotatedStackTrace.throwableArgs(t, args);
        for (StackTraceElement element : t.getStackTrace()) {
            AnnotatedStackTraceElement annotatedElement = AnnotatedStackTraceElement.of(element);
            equals((StackTraceElement) trace.args.get(annotatedElement)[0],element);
        }
    }
    
    static void equals(StackTraceElement a, StackTraceElement b) {
         assertEquals(a,b);
    }

    @Test
    public void twoTracesWithSameThrowableAndArgsAreEqual() {
        Throwable t = new Throwable();
        Map<StackTraceElement,Object[]> args = Maps.newHashMap();
        for (StackTraceElement element : t.getStackTrace()) {
            args.put(element, new Object[] { element });
        }
        AnnotatedStackTrace a = AnnotatedStackTrace.throwableArgs(t, args);
        AnnotatedStackTrace b = AnnotatedStackTrace.throwableArgs(t, args);
        assertEquals(a,b);
    }
}

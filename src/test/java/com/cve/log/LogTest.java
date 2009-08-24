package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import junit.framework.Assert;
import org.junit.Test;


/**
 *
 * @author curt
 */
public class LogTest {

    @Test
    public void annotatedStackTraceRetainsMethodArgSomewhere() {
        String ARG = "arg";
        Log.note(ARG);
        Throwable t = new Throwable();
        AnnotatedStackTrace trace = Log.annotatedStackTrace(t);
        for (StackTraceElement element : trace.elements) {
            Object[] args = trace.args.get(element);
            if (args!=null && args.length==1 && args[0].equals(ARG)) {
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void annotatedStackTraceRetainsMethodArgHere() {
        String ARG = "arg";
        Log.note(ARG);
        Throwable t = new Throwable();
        AnnotatedStackTrace trace = Log.annotatedStackTrace(t);
        StackTraceElement element = trace.elements.get(0);
        Assert.assertEquals(ARG, trace.args.get(element)[0]);
    }

}

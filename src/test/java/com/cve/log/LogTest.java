package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import junit.framework.Assert;
import org.junit.Test;


/**
 *
 * @author curt
 */
public class LogTest {

    final Log log = null;

    @Test
    public void annotatedStackTraceRetainsMethodArgSomewhere() {
        String ARG = "arg";
        Throwable t = methodThatTakesAn(ARG);
        AnnotatedStackTrace trace = log.annotatedStackTrace(t);
        for (StackTraceElement element : trace.elements) {
            Object[] args = trace.args.get(element);
            if (args!=null && args.length==1 && args[0].equals(ARG)) {
                return;
            }
        }
        Assert.fail();
    }

    Throwable methodThatTakesAn(Object arg) {
        log.args(arg);
        return new Throwable();
    }

    @Test
    public void annotatedStackTraceRetainsMethodArgHereWithDirectNote() {
        String ARG = "arg";
        Throwable t = methodThatTakesAn(ARG);
        AnnotatedStackTrace trace = log.annotatedStackTrace(t);
        StackTraceElement element = trace.elements.get(1);
        Object[] args = trace.args.get(element);
        Assert.assertEquals(ARG, args[0]);
    }

}

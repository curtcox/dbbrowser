package com.cve.log;

import com.cve.lang.AnnotatedCallTree;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author curt
 */
public class SimpleLogTest {

    @Test
    public void annotatedCallTreeIsNotNull() {
        Log log = SimpleLog.of();
        AnnotatedCallTree tree = log.annotatedCallTree();
        Assert.assertNotNull(tree);
    }
}

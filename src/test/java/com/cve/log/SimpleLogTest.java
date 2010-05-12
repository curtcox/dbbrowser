package com.cve.log;

import com.cve.lang.AnnotatedCallTree;
import com.cve.model.test.Assert;
import org.junit.Test;

/**
 *
 * @author curt
 */
public class SimpleLogTest {

    final Log log = SimpleLog.of();

    @Test
    public void treeIsNotNull() {
        AnnotatedCallTree tree = log.annotatedCallTree();
        Assert.notNull(tree);
    }

    @Test
    public void treeContainsMethodWithNoArgs() {
        methodWithNoArgs();
        AnnotatedCallTree tree = log.annotatedCallTree();
        Assert.notNull(tree);
    }

    void methodWithNoArgs() {
        log.args();
    }


}

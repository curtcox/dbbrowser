package com.cve.lang;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * A tree of annotated stack trace elements.
 * @author curt
 */
@Immutable
public final class AnnotatedCallTree {

    /**
     * The object at the root of this tree.
     */
    final AnnotatedStackTraceElement root;

    /**
     * The sub-trees immediately under the root.
     */
    final ImmutableList<AnnotatedStackTraceElement> subTrees;

    private AnnotatedCallTree(AnnotatedStackTraceElement root, List<AnnotatedStackTraceElement> children) {
        this.root = Check.notNull(root);
        this.subTrees = ImmutableList.copyOf(children);
    }

    public static AnnotatedCallTree of(AnnotatedStackTraceElement root,List<AnnotatedStackTraceElement> children) {
        return new AnnotatedCallTree(root,children);
    }
}

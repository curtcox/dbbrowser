package com.cve.ui.swing;

import com.cve.util.Check;
import java.util.Arrays;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * For debugging tree models.
 * @author curt
 */
final class DebugTreeModel implements TreeModel {

    final TreeModel model;

    private DebugTreeModel(TreeModel model) {
        this.model = Check.notNull(model);
    }

    static DebugTreeModel of(TreeModel model) {
        return new DebugTreeModel(model);
    }

    @Override
    public Object getRoot() {
        return print(model.getRoot(),"getRoot");
    }

    @Override
    public Object getChild(Object o, int i) {
        return print(model.getChild(o,i),"getChild",o,i);
    }

    @Override
    public int getChildCount(Object o) {
        return print(model.getChildCount(o),"getChildCount",o);
    }

    @Override
    public boolean isLeaf(Object o) {
        return print(model.isLeaf(o),"isLeaf",o);
    }

    @Override
    public void valueForPathChanged(TreePath tp, Object o) {
        model.valueForPathChanged(tp, o);
    }

    @Override
    public int getIndexOfChild(Object o, Object o1) {
        return print(model.getIndexOfChild(o, o1),"getIndexOfChild");
    }

    @Override
    public void addTreeModelListener(TreeModelListener tl) {
        model.addTreeModelListener(tl);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener tl) {
        model.removeTreeModelListener(tl);
    }

    static <T> T print(T t, String method, Object... args) {
        System.out.println(method + "(" + Arrays.asList(args) + ") : " + t);
        return t;
    }
}

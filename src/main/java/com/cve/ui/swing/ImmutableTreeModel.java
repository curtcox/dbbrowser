package com.cve.ui.swing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * An immutable tree model.
 * @author curt
 */
@Immutable
public final class ImmutableTreeModel implements TreeModel {

    final Object root;
    final Multimap kids;
    final List<TreeModelListener> listeners = Lists.newArrayList();

    private ImmutableTreeModel(Object root, Multimap children) {
        this.root = root;
        this.kids = ImmutableMultimap.copyOf(children);
    }

    @Override public Object getRoot() { return root; }

    @Override public Object getChild(Object o, int i) {
        return kids.get(o).toArray()[i];
    }

    @Override
    public int getIndexOfChild(Object o, Object child) {
        return ImmutableList.of(kids.get(o).toArray()).indexOf(child);
    }

    @Override
    public int getChildCount(Object o) {
        return kids.get(o).size();
    }

    @Override
    public boolean isLeaf(Object o) {
        return kids.get(o).isEmpty();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object o) {
        TreeModelEvent event = new TreeModelEvent(o,path);
        for (TreeModelListener listener : listeners) {
            listener.treeNodesChanged(event);
        }
    }


    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

}

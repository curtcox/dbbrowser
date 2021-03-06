package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.util.Check;

/**
 *
 * @author Curt
 */
public final class SynchronizedLayoutManager implements UILayout.Manager {

    final UILayout.Manager inner;

    SynchronizedLayoutManager(UILayout.Manager inner) {
        this.inner = Check.notNull(inner);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        inner.addLayoutComponent(name, comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        inner.removeLayoutComponent(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return inner.preferredLayoutSize(parent);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return inner.minimumLayoutSize(parent);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            inner.layoutContainer(parent);
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Constraint constraints) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void invalidateLayout(Container target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

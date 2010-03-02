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
public final class DebugLayoutManager implements UILayout.Manager {

    final UILayout.Manager inner;

    DebugLayoutManager(UILayout.Manager inner) {
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
        Dimension d = inner.preferredLayoutSize(parent);
        debug(parent + " preferred size " + d);
        return d;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension d = inner.minimumLayoutSize(parent);
        debug(parent + " minimum size " + d);
        return d;
    }

    @Override
    public void layoutContainer(Container parent) {
        inner.layoutContainer(parent);
    }

    void debug(String message) {
        System.out.println(message);
    }

    @Override
    public void addLayoutComponent(Component comp, Constraint constraints) {
        inner.addLayoutComponent(comp, constraints);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return inner.maximumLayoutSize(target);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return inner.getLayoutAlignmentX(target);
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return inner.getLayoutAlignmentY(target);
    }

    @Override
    public void invalidateLayout(Container target) {
        inner.invalidateLayout(target);
    }
}

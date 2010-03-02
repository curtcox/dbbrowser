package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Bounds;
import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Wrapper that turns a UILayout.Function into a UILayout.Manager.
 * @author curt
 */
public final class FunctionalLayoutManager implements UILayout.Manager {

    final UILayout.Function function;

    final Map<Component,Constraint> constraints = Maps.newHashMap();

    private FunctionalLayoutManager(UILayout.Function function) {
        this.function = Check.notNull(function);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addLayoutComponent(Component comp, Constraint constraint) {
        constraints.put(comp,constraint);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        constraints.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension size = parent.getSize();
        ImmutableMap<Component,Bounds> layout = function.layout(parent, size, ImmutableMap.copyOf(constraints));
        for (Component component : layout.keySet()) {
            Bounds bounds = layout.get(component); 
            component.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        }
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

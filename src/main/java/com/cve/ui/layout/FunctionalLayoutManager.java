package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Bounds;
import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.ui.layout.UILayout.Insets;
import com.cve.ui.layout.UILayout.Manager;
import com.cve.util.Check;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper that turns a UILayout.Function into a UILayout.Manager.
 * @author curt
 */
public final class FunctionalLayoutManager implements UILayout.Manager {

    final UILayout.Function function;

    final List<Component> components = Lists.newArrayList();

    final Map<Component,Constraint> constraints = Maps.newHashMap();

    private FunctionalLayoutManager(UILayout.Function function) {
        this.function = Check.notNull(function);
    }

    static Manager of(FlowFunction function) {
        return new FunctionalLayoutManager(function);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addLayoutComponent(Component comp, Constraint constraint) {
        if (constraint==null) {
            constraints.put(comp,Constraint.NULL);
        } else {
            constraints.put(comp,constraint);
        }
        components.add(comp);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        constraints.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        int w = 0;
        int h = 0;
        ImmutableMap<Component,Bounds> layout = layout(parent);
        for (Component component : layout.keySet()) {
            Bounds bounds = layout.get(component);
            w = Math.max(w, bounds.width);
            h = Math.max(h, bounds.height);
        }
        return Dimension.of(w,h);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }

    @Override
    public void layoutContainer(Container parent) {
        ImmutableMap<Component,Bounds> layout = layout(parent);
        for (Component component : layout.keySet()) {
            Bounds bounds = layout.get(component); 
            component.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    ImmutableMap<Component,Bounds> layout(Container parent) {
        validate(parent);
        Dimension size = parent.getSize();
        ImmutableMap<Component,Constraint> current = ImmutableMap.copyOf(constraints);
        Insets                              insets = parent.getInsets();
        ImmutableMap<Component,Bounds> layout = function.layout(current, insets, size);
        Set<Component> missing = Sets.newHashSet();

        missing.addAll(current.keySet());
        missing.removeAll(layout.keySet());
        if (!missing.isEmpty()) {
            String message = "Missing " + missing;
            throw new IllegalStateException(message);
        }

        Set<Component> extra = Sets.newHashSet();
        extra.addAll(layout.keySet());
        extra.removeAll(current.keySet());
        if (!extra.isEmpty()) {
            String message = "Extra " + extra;
            throw new IllegalStateException(message);
        }
        return layout;
    }

    void validate(Container container) {
        Set<Component> contents = ImmutableSet.copyOf(container.getComponents());
        Set<Component> constrained = constraints.keySet();
        if (contents.equals(constrained)) {
            return;
        }
        String message = contents + "!=" + constrained;
        throw new IllegalStateException(message);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
        // the function should be stateless
    }

}

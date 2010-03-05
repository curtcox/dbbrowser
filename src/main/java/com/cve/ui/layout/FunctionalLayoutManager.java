package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Bounds;
import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.ui.layout.UILayout.Insets;
import com.cve.ui.layout.UILayout.Manager;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
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

    /**
     * The function that will be used
     */
    final UILayout.Function function;

    /**
     * The components of the container being laid out
     */
    final List<Component> components = Lists.newArrayList();

    /**
     * The constraints to give the function
     */
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
        Insets insets = parent.getInsets();
        for (Component component : layout.keySet()) {
            Bounds bounds = layout.get(component);
            Component writable = ((ReadOnlyComponent) component).inner;
            writable.setBounds(bounds.x + insets.left, bounds.y + insets.top, bounds.width, bounds.height);
        }
    }

    ImmutableMap<Component,Bounds> layout(Container parent) {
        validate(parent);
        Dimension size = parent.getSize();
        ImmutableList<Component>            componentsNow = readOnly(components);
        ImmutableMap<Component,Constraint> constraintsNow = readOnly(constraints);
        Insets                              insets = parent.getInsets();
        Dimension sizeMinusInsets = Dimension.of(size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
        ImmutableMap<Component,Bounds> layout =
            function.layout(componentsNow, constraintsNow, sizeMinusInsets);
        Set<Component> missing = Sets.newHashSet();

        missing.addAll(componentsNow);
        missing.removeAll(layout.keySet());
        if (!missing.isEmpty()) {
            String message = "Missing components " + missing;
            throw new IllegalStateException(message);
        }

        Set<Component> extra = Sets.newHashSet();
        extra.addAll(layout.keySet());
        extra.removeAll(componentsNow);
        if (!extra.isEmpty()) {
            String message = "Extra components " + extra;
            throw new IllegalStateException(message);
        }
        return layout;
    }

    ImmutableList<Component> readOnly(List<Component> components) {
        List<Component> copy = Lists.newArrayList();
        for (Component component : components) {
            copy.add(readOnly(component));
        }
        return ImmutableList.copyOf(copy);
    }

    ImmutableMap<Component,Constraint> readOnly(Map<Component,Constraint> components) {
        Map<Component,Constraint> copy = Maps.newHashMap();
        for (Component component : components.keySet()) {
            Component key = readOnly(component);
            Constraint value = components.get(component);
            copy.put(key, value);
        }
        return ImmutableMap.copyOf(copy);
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

    final Map<Component,ReadOnlyComponent> readOnly = Maps.newHashMap();
    ReadOnlyComponent readOnly(Component component) {
        ReadOnlyComponent wrapper = readOnly.get(component);
        if (wrapper!=null) {
            return wrapper;
        }
        wrapper = ReadOnlyComponent.of(component);
        readOnly.put(component, wrapper);
        return wrapper;
    }

/**
 * We give functions wrapped components, to make sure they don't update them.
 */
private static final class ReadOnlyComponent implements Component {

    final Component inner;

    ReadOnlyComponent(Component component) {
        inner = Check.notNull(component);
    }

    static ReadOnlyComponent of(Component component) {
        return new ReadOnlyComponent(component);
    }

    // Getters are OK
    @Override public Dimension getSize()          { return inner.getSize(); }
    @Override public boolean isVisible()          { return inner.isVisible();  }
    @Override public Dimension getPreferredSize() { return inner.getPreferredSize(); }
    @Override public Dimension getMinimumSize()   { return inner.getMinimumSize(); }
    @Override public int getBaseline(int width, int height) { return inner.getBaseline(width, height);  }
    // Setters are forbidden
    @Override public void setSize(int width, int height)                 { throw new UnsupportedOperationException(); }
    @Override public void setLocation(int x, int cy)                     { throw new UnsupportedOperationException(); }
    @Override public void setBounds(int x, int y, int width, int height) { throw new UnsupportedOperationException(); }
    @Override public String toString() {  return "readOnly(" + inner +")";  }

}

}

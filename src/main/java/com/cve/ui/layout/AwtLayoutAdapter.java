package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Insets;
import com.cve.ui.layout.UILayout.Orientation;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.List;
import java.util.Map;

/**
 * Allows UILayout.ManagerS to be used as LayoutManagers.
 * @author Curt
 */
public final class AwtLayoutAdapter implements LayoutManager2 {

    public final UILayout.Manager manager;

    private AwtLayoutAdapter(UILayout.Manager manager) {
        this.manager = Check.notNull(manager);
    }

    public static AwtLayoutAdapter of(UILayout.Manager manager) {
        return new AwtLayoutAdapter(manager);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        manager.addLayoutComponent(name, component(comp));
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        manager.removeLayoutComponent(component(comp));
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return dimension(manager.preferredLayoutSize(container(parent)));
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return dimension(manager.minimumLayoutSize(container(parent)));
    }

    @Override
    public void layoutContainer(Container parent) {
        manager.layoutContainer(container(parent));
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        manager.addLayoutComponent(component(comp),constraints);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return dimension(manager.maximumLayoutSize(container(target)));
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return manager.getLayoutAlignmentX(container(target));
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return manager.getLayoutAlignmentY(container(target));
    }

    @Override
    public void invalidateLayout(Container target) {
        manager.invalidateLayout(container(target));
    }


static final class AwtContainerAdapter implements UILayout.Container {

    final Container container;
    static final Map<Component,AwtContainerAdapter> adapters = Maps.newHashMap();

    AwtContainerAdapter(Container container) {
        this.container = Check.notNull(container);
    }

    static UILayout.Container of(Container container) {
        if (adapters.containsKey(container)) {
            return adapters.get(container);
        }
        AwtContainerAdapter adapter = new AwtContainerAdapter(container);
        adapters.put(container, adapter);
        return adapter;
    }

    @Override public Object getTreeLock() { return container.getTreeLock();  }
    @Override public int       getWidth() { return container.getWidth(); }
    @Override public int      getHeight() { return container.getHeight(); }
    @Override public Insets   getInsets() { return insets(container.getInsets());  }
    @Override public UILayout.Dimension getSize() {
        return dimension(container.getSize());
    }

    @Override
    public ImmutableList<UILayout.Component> getComponents() {
        List<UILayout.Component> list = Lists.newArrayList();
        for (Component component : container.getComponents()) {
            list.add(component(component));
        }
        return ImmutableList.copyOf(list);
    }

    @Override
    public Orientation getComponentOrientation() {
        return orientation(container.getComponentOrientation());
    }


    }

static final class AwtComponentAdapter implements UILayout.Component {

    final Component component;

    static final Map<Component,AwtComponentAdapter> adapters = Maps.newHashMap();

    private AwtComponentAdapter(Component component) {
        this.component = Check.notNull(component);
    }

    static UILayout.Component of(Component component) {
        if (adapters.containsKey(component)) {
            return adapters.get(component);
        }
        AwtComponentAdapter adapter = new AwtComponentAdapter(component);
        adapters.put(component, adapter);
        return adapter;
    }

    @Override public boolean isVisible() { return component.isVisible();  }
    @Override public UILayout.Dimension getPreferredSize() {
        return dimension(component.getPreferredSize());
    }

    @Override
    public int getBaseline(int width, int height) {
        return component.getBaseline(width, height);
    }

    @Override
    public void setSize(int width, int height) {
        component.setSize(width,height);
    }

    @Override
    public UILayout.Dimension getMinimumSize() {
        return dimension(component.getMinimumSize());
    }

    @Override
    public void setLocation(int x, int cy) {
        component.setLocation(x, cy);
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        component.setBounds(x, y,w,h);
    }

    @Override
    public int getHeight() {
        return component.getHeight();
    }

    @Override
    public int getWidth() {
        return component.getWidth();
    }
}

    static final UILayout.Component component(Component comp) {
        return AwtComponentAdapter.of(comp);
    }

    static final UILayout.Container container(Container container) {
        return AwtContainerAdapter.of(container);
    }

    static final Dimension dimension(UILayout.Dimension dim) {
        return new Dimension(dim.width,dim.height);
    }

    static final UILayout.Dimension dimension(Dimension dim) {
        return UILayout.Dimension.of(dim.width,dim.height);
    }

    static final UILayout.Insets insets(java.awt.Insets insets) {
        return UILayout.Insets.of(insets.top,insets.bottom,insets.left,insets.right);
    }

    static final UILayout.Orientation orientation(java.awt.ComponentOrientation orientation) {
        return UILayout.Orientation.of(orientation.isLeftToRight());
    }

}

package com.cve.ui.layout;

import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;

/**
 * A set of interfaces for simplifying the AWT layout process.
 * This:
 * <ol>
 * <li> makes it explicit what Component and Container methods are actually
 * being used for layout.
 * <li> provides immutable counterparts for orientation, insets, and dimension.
 * <li> allows for easy insertion of debugging code.
 * </ol>
 * See AwtLayoutAdapter to use with AWT or Swing.
 * @author Curt
 */
public interface UILayout {

/**
 * This interface corresponsds to java.awt.LayoutManager2.
 */
interface Manager {

    /**
     * If the layout manager uses a per-component string, adds the component comp
     * to the layout, associating it with the string specified by name.
     */
    void addLayoutComponent(String name, Component comp);

    /**
     * Adds the specified component to the layout, using the specified constraint object.
     */
    void addLayoutComponent(Component comp, Constraint constraints);

    /**
    Removes the specified component from the layout.
    */
    void removeLayoutComponent(Component comp);

    /**
     * Calculates the preferred size dimensions for the specified container, given the components it contains.
     */
    Dimension preferredLayoutSize(Container parent);

    /**
    Calculates the minimum size dimensions for the specified container, given the components it contains.
    */
    Dimension minimumLayoutSize(Container parent);

    /**
     * Calculates the maximum size dimensions for the specified container, given the components it contains.
     */
    Dimension maximumLayoutSize(Container target);

    /**
    Lays out the specified container.
    */
    void layoutContainer(Container parent);


    /**
     * Returns the alignment along the x axis. This specifies how the component
     * would like to be aligned relative to other components. The value should
     * be a number between 0 and 1 where 0 represents alignment along the origin,
     * is aligned the furthest away from the origin, 0.5 is centered, etc.
     */
    float getLayoutAlignmentX(Container target);

    /**
     * Returns the alignment along the y axis. This specifies how the component
     * would like to be aligned relative to other components. The value should
     * be a number between 0 and 1 where 0 represents alignment along the origin,
     * 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
     */
    float getLayoutAlignmentY(Container target);

    /**
     * Invalidates the layout, indicating that if the layout manager has cached
     * information it should be discarded.
     */
    void invalidateLayout(Container target);
}

/**
 * A constraint on how a component is to be layed out.
 */
interface Constraint {}

/**
 * This interface represents the methods Managers actually use from the
 * components they are laying out.
 */
interface Component {

    boolean isVisible();

    /**
     * Gets the preferred size of this component.
     */
    Dimension getPreferredSize();

    /**
     * Gets the minimum size of this component.
     */
    Dimension getMinimumSize();

    /**
     * Returns the baseline. The baseline is measured from the top of the component.
     * This method is primarily meant for LayoutManagers to align components along their baseline.
     * A return value less than 0 indicates this component does not have a reasonable
     * baseline and that LayoutManagers should not align this component on its baseline.
     * <p>
     * The default implementation returns -1.  Subclasses that support baseline
     * should override appropriately. If a value >= 0 is returned, then the
     * component has a valid baseline for any size >= the minimum size and
     * getBaselineResizeBehavior can be used to determine how the baseline
     * changes with size.
     */
    int getBaseline(int width, int height);

    /**
     * Resizes this component so that it has width width and height height.
     */
    void setSize(int width, int height);

    /**
     * Moves this component to a new location. The top-left corner of the new
     * location is specified by the x and y  parameters in the coordinate space
     * of this component's parent.
     */
    void setLocation(int x, int cy);

    /**
     * Moves and resizes this component. The new location of the top-left corner
     * is specified by x and y, and the new size is specified by width and height.
     */
    void setBounds(int x, int y, int width, int height);

    Dimension getSize();
}

/**
 * This interface represents the methods Managers actually use on the conatiners
 * they are laying out.  While java.awt.Container extends java.awt.Component,
 * this intentionally does not extends Component.  This is done because apart
 * from getSize(), the methods for layout are entirely different between a
 * conatiner being layed out and its components.  Of course a container could
 * well be a component in another container.
 */
interface Container {

    /**
     * Gets this component's locking object
     * (the object that owns the thread sychronization monitor) for AWT
     * component-tree and layout operations.
     */
    Object getTreeLock();

    /**
     * Determines the insets of this container, which indicate the size of the container's border.
     * A Frame object, for example, has a top inset that corresponds to the height of the frame's title bar.
     */
    Insets getInsets();

    /**
     * Return a list of the components that need to be layed out.
     */
    ImmutableList<Component> getComponents();

    Orientation getComponentOrientation();

    Dimension getSize();
}

@Immutable
final class Orientation {

    final boolean leftToRight;

    private Orientation(boolean leftToRight) {
        this.leftToRight = leftToRight;
    }

    static Orientation of(boolean leftToRight) {
        return new Orientation(leftToRight);
    }
}

/**
 * An Insets object is a representation of the borders of a container.
 * It specifies the space that a container must leave at each of its edges.
 * The space can be a border, a blank space, or a title.
 */
@Immutable
final class Insets {
    final int top;
    final int bottom;
    final int left;
    final int right;

    private Insets(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    static Insets of(int top, int bottom, int left, int right) {
        return new Insets(top,bottom,left,right);
    }
}

@Immutable
final class Dimension {

    final int width;
    final int height;

    private Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    static Dimension of(int width, int height) {
        return new Dimension(width,height);
    }
}

}

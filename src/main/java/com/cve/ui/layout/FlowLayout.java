/*
 * @(#)FlowLayout.java	1.58 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.ui.layout.UILayout.Insets;
import static java.lang.Math.max;

/**
 * A flow layout arranges components in a directional flow, much
 * like lines of text in a paragraph. The flow direction is
 * determined by the container's <code>componentOrientation</code>
 * property and may be one of two values:
 * <ul>
 * <li><code>ComponentOrientation.LEFT_TO_RIGHT</code>
 * <li><code>ComponentOrientation.RIGHT_TO_LEFT</code>
 * </ul>
 * Flow layouts are typically used
 * to arrange buttons in a panel. It arranges buttons
 * horizontally until no more buttons fit on the same line.
 * The line alignment is determined by the <code>align</code>
 * property. The possible values are:
 * <ul>
 * <li>{@link #LEFT LEFT}
 * <li>{@link #RIGHT RIGHT}
 * <li>{@link #CENTER CENTER}
 * <li>{@link #LEADING LEADING}
 * <li>{@link #TRAILING TRAILING}
 * </ul>
 * <p>
 * For example, the following picture shows an applet using the flow
 * layout manager (its default layout manager) to position three buttons:
 * <p>
 * <img src="doc-files/FlowLayout-1.gif"
 * ALT="Graphic of Layout for Three Buttons"
 * ALIGN=center HSPACE=10 VSPACE=7>
 * <p>
 * Here is the code for this applet:
 * <p>
 * <hr><blockquote><pre>
 * import java.awt.*;
 * import java.applet.Applet;
 *
 * public class myButtons extends Applet {
 *     Button button1, button2, button3;
 *     public void init() {
 *         button1 = new Button("Ok");
 *         button2 = new Button("Open");
 *         button3 = new Button("Close");
 *         add(button1);
 *         add(button2);
 *         add(button3);
 *     }
 * }
 * </pre></blockquote><hr>
 * <p>
 * A flow layout lets each component assume its natural (preferred) size.
 *
 * @version     1.58, 11/17/05
 * @author      Arthur van Hoff
 * @author      Sami Shaio
 * @since       JDK1.0
 * @see ComponentOrientation
 *
 * See also "JScrollPane and FlowLayout do not interact properly"
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5082531
 */
public final class FlowLayout implements UILayout.Manager {

    public static FlowLayout of() {
        return new FlowLayout();
    }

public enum Align {
    /**
     * This value indicates that each row of components
     * should be left-justified.
     */
    LEFT(0),

    /**
     * This value indicates that each row of components
     * should be centered.
     */
    CENTER(1),

    /**
     * This value indicates that each row of components
     * should be right-justified.
     */
    RIGHT(2),

    /**
     * This value indicates that each row of components
     * should be justified to the leading edge of the container's
     * orientation, for example, to the left in left-to-right orientations.
     *
     * @see     java.awt.Component#getComponentOrientation
     * @see     java.awt.ComponentOrientation
     * @since   1.2
     */
    LEADING(3),

    /**
     * This value indicates that each row of components
     * should be justified to the trailing edge of the container's
     * orientation, for example, to the right in left-to-right orientations.
     *
     * @see     java.awt.Component#getComponentOrientation
     * @see     java.awt.ComponentOrientation
     * @since   1.2
     */
    TRAILING(4);

    int value;

    Align(int value) {
        this.value = value;
    }
}

    /**
     * <code>newAlign</code> is the property that determines
     * how each row distributes empty space for the Java 2 platform,
     * v1.2 and greater.
     * It can be one of the following three values:
     * <ul>
     * <code>LEFT</code>
     * <code>RIGHT</code>
     * <code>CENTER</code>
     * <code>LEADING</code>
     * <code>TRAILING</code>
     * </ul>
     *
     * @serial
     * @since 1.2
     * @see #getAlignment
     * @see #setAlignment
     */
    final Align newAlign;       // This is the one we actually use

    /**
     * The flow layout manager allows a seperation of
     * components with gaps.  The horizontal gap will
     * specify the space between components and between
     * the components and the borders of the
     * <code>Container</code>.
     *
     * @serial
     * @see #getHgap()
     * @see #setHgap(int)
     */
    final int hgap;

    /**
     * The flow layout manager allows a seperation of
     * components with gaps.  The vertical gap will
     * specify the space between rows and between the
     * the rows and the borders of the <code>Container</code>.
     *
     * @serial
     * @see #getHgap()
     * @see #setHgap(int)
     */
    public final int vgap;

    /**
     * If true, components will be aligned on their baseline.
     */
    final boolean alignOnBaseline;

    /*
     * JDK 1.1 serialVersionUID
     */
     private static final long serialVersionUID = -7262534875583282631L;

    /**
     * Constructs a new <code>FlowLayout</code> with a centered alignment and a
     * default 5-unit horizontal and vertical gap.
     */
    private FlowLayout() {
        this(Align.LEFT, 5, 5);
    }

    /**
     * Constructs a new <code>FlowLayout</code> with the specified
     * alignment and a default 5-unit horizontal and vertical gap.
     * The value of the alignment argument must be one of
     * <code>FlowLayout.LEFT</code>, <code>FlowLayout.RIGHT</code>,
     * <code>FlowLayout.CENTER</code>, <code>FlowLayout.LEADING</code>,
     * or <code>FlowLayout.TRAILING</code>.
     * @param align the alignment value
     */
    private FlowLayout(Align align) {
        this(align, 5, 5);
    }

    /**
     * Creates a new flow layout manager with the indicated alignment
     * and the indicated horizontal and vertical gaps.
     * <p>
     * The value of the alignment argument must be one of
     * <code>FlowLayout.LEFT</code>, <code>FlowLayout.RIGHT</code>,
     * <code>FlowLayout.CENTER</code>, <code>FlowLayout.LEADING</code>,
     * or <code>FlowLayout.TRAILING</code>.
     * @param      align   the alignment value
     * @param      hgap    the horizontal gap between components
     *                     and between the components and the
     *                     borders of the <code>Container</code>
     * @param      vgap    the vertical gap between components
     *                     and between the components and the
     *                     borders of the <code>Container</code>
     */
    private FlowLayout(Align align, int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
        this.newAlign = align;
        alignOnBaseline = false;
    }

    /**
     * Adds the specified component to the layout.
     * Not used by this class.
     * @param name the name of the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout.
     * Not used by this class.
     * @param comp the component to remove
     * @see       java.awt.Container#removeAll
     */
    @Override
    public void removeLayoutComponent(Component comp) {
    }

/**
 * Returns the preferred dimensions for this layout given the
 * <i>visible</i> components in the specified target container.
 *
 * @param target the container that needs to be laid out
 * @return    the preferred dimensions to lay out the
 *            subcomponents of the specified container
 * @see Container
 * @see #minimumLayoutSize
 * @see       java.awt.Container#getPreferredSize
 */
@Override
public Dimension preferredLayoutSize(Container target) {
    // is this the first visible component?
    boolean firstVisibleComponent = true;
    int maxAscent = 0;
    int maxDescent = 0;
    int width = 0;
    int height = 0;

    // for every visible component
    for (Component m : target.getComponents()) {
        if (m.isVisible()) {
            Dimension d = m.getPreferredSize();
            height = max(height, d.height);
            if (firstVisibleComponent) {
                firstVisibleComponent = false;
            } else {
                width += hgap;
            }
            width += d.width;
            if (alignOnBaseline) {
                int baseline = m.getBaseline(d.width, d.height);
                if (baseline >= 0) {
                    maxAscent = max(maxAscent, baseline);
                    maxDescent = max(maxDescent, d.height - baseline);
                }
            }
        }
    }
    if (alignOnBaseline) {
        height = max(maxAscent + maxDescent, height);
    }
    Insets insets = target.getInsets();
    width += insets.left + insets.right + hgap*2;
    height += insets.top + insets.bottom + vgap*2;
    return Dimension.of(width, height);
}

/**
 * Returns the minimum dimensions needed to layout the <i>visible</i>
 * components contained in the specified target container.
 * @param target the container that needs to be laid out
 * @return    the minimum dimensions to lay out the
 *            subcomponents of the specified container
 * @see #preferredLayoutSize
 * @see       java.awt.Container
 * @see       java.awt.Container#doLayout
 */
@Override
public Dimension minimumLayoutSize(Container target) {
    int maxAscent = 0;
    int maxDescent = 0;
    boolean firstVisibleComponent = true;
    int width = 0;
    int height = 0;
    for (Component m : target.getComponents()) {
        if (m.isVisible()) {
            Dimension d = m.getMinimumSize();
            height = max(height, d.height);
            if (firstVisibleComponent) {
                firstVisibleComponent = false;
            } else {
                width += hgap;
            }
            width += d.width;
            if (alignOnBaseline) {
                int baseline = m.getBaseline(d.width, d.height);
                if (baseline >= 0) {
                    maxAscent = max(maxAscent, baseline);
                    maxDescent = max(maxDescent, height - baseline);
                }
            }
        }
    }

    if (alignOnBaseline) {
        height = max(maxAscent + maxDescent, height);
    }

    Insets insets = target.getInsets();
    width += insets.left + insets.right + hgap*2;
    height += insets.top + insets.bottom + vgap*2;
    return Dimension.of(width, height);
}

/**
 * Centers the elements in the specified row, if there is any slack.
 * @param target the component which needs to be moved
 * @param x the x coordinate
 * @param y the y coordinate
 * @param width the width dimensions
 * @param height the height dimensions
 * @param rowStart the beginning of the row
 * @param rowEnd the the ending of the row
 * @param useBaseline Whether or not to align on baseline.
 * @param ascent Ascent for the components. This is only valid if
 *               useBaseline is true.
 * @param descent Ascent for the components. This is only valid if
 *               useBaseline is true.
 * @return actual row height
 */
int moveComponents(
    final Container target,
    final int ix, final int y, final int width, final int iheight,
    final int rowStart, final int rowEnd, final boolean ltr,
    final boolean useBaseline, final int[] ascent,
    final int[] descent)
{
    int x = ix;
    int height = iheight;
    switch (newAlign) {
        case LEFT:
            x += ltr ? 0 : width;
            break;
        case CENTER:
            x += width / 2;
            break;
        case RIGHT:
            x += ltr ? width : 0;
            break;
        case LEADING:
            break;
        case TRAILING:
            x += width;
            break;
    }
    int maxAscent = 0;
    int nonbaselineHeight = 0;
    int baselineOffset = 0;
    if (useBaseline) {
        int maxDescent = 0;
        for (int i = rowStart ; i < rowEnd ; i++) {
            Component m = target.getComponents().get(i);
            if (m.isVisible()) {
                if (ascent[i] >= 0) {
                    maxAscent = max(maxAscent, ascent[i]);
                    maxDescent = max(maxDescent, descent[i]);
                } else {
                    nonbaselineHeight = max(m.getHeight(),
                                                 nonbaselineHeight);
                }
            }
        }
        height = max(maxAscent + maxDescent, nonbaselineHeight);
        baselineOffset = (height - maxAscent - maxDescent) / 2;
    }
    // for every visible component
    for (int i = rowStart ; i < rowEnd ; i++) {
        Component m = target.getComponents().get(i);
        if (m.isVisible()) {
            int cy;
            if (useBaseline && ascent[i] >= 0) {
                cy = y + baselineOffset + maxAscent - ascent[i];
            } else {
                cy = y + (height - m.getHeight()) / 2;
            }
            if (ltr) {
            m.setLocation(x, cy);
            } else {
            m.setLocation(target.getWidth() - x - m.getWidth(), cy);
            }
            x += m.getWidth() + hgap;
        }
    }
    return height;
}

/**
 * Lays out the container. This method lets each
 * <i>visible</i> component take
 * its preferred size by reshaping the components in the
 * target container in order to satisfy the alignment of
 * this <code>FlowLayout</code> object.
 *
 * @param target the specified component being laid out
 * @see Container
 * @see       java.awt.Container#doLayout
 */
@Override
public void layoutContainer(final Container target) {
    Insets insets = target.getInsets();
    int maxwidth = target.getWidth() - (insets.left + insets.right + hgap*2);
    int nmembers = target.getComponents().size();
    int x = 0;
    int y = insets.top + vgap;
    int rowh = 0;
    int start = 0;

    boolean ltr = target.getComponentOrientation().leftToRight;

    int[] ascent = null;
    int[] descent = null;

    if (alignOnBaseline) {
        ascent = new int[nmembers];
        descent = new int[nmembers];
    }

    // for every visible component
    for (int i = 0 ; i < nmembers ; i++) {
        Component m = target.getComponents().get(i);
        if (m.isVisible()) {
            Dimension d = m.getPreferredSize();
            m.setSize(d.width, d.height);

            if (alignOnBaseline) {
                int baseline = m.getBaseline(d.width, d.height);
                if (baseline >= 0) {
                    ascent[i] = baseline;
                    descent[i] = d.height - baseline;
                } else {
                    ascent[i] = -1;
                }
            }
            // if there is more space on this row
            if ((x == 0) || ((x + d.width) <= maxwidth)) {
                if (x > 0) {
                    x += hgap;
                }
                x += d.width;
                rowh = Math.max(rowh, d.height);
            } else { // else finish the row
                rowh = moveComponents(target,
                    // x, y, w, h
                    insets.left + hgap, y,  maxwidth - x, rowh,
                    start, i,
                    ltr, alignOnBaseline, ascent, descent);
                x = d.width;
                y += vgap + rowh;
                rowh = d.height;
                start = i;
            }
        }
    }
    moveComponents(target, insets.left + hgap, y, maxwidth - x, rowh,
                   start, nmembers, ltr, alignOnBaseline, ascent, descent);
}

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {

    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
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
    public void invalidateLayout(Container target) {}


    /**
     * Returns a string representation of this <code>FlowLayout</code>
     * object and its values.
     * @return     a string representation of this layout
     */
    @Override
    public String toString() {
        String str = "";
        switch (newAlign) {
          case LEFT:        str = ",align=left"; break;
          case CENTER:      str = ",align=center"; break;
          case RIGHT:       str = ",align=right"; break;
          case LEADING:     str = ",align=leading"; break;
          case TRAILING:    str = ",align=trailing"; break;
        }
        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + str + "]";
    }


}

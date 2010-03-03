/*
 * @(#)FlowLayout.java	1.58 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.cve.ui.layout;

import com.google.common.collect.Maps;
import java.util.Map;
import com.cve.ui.layout.UILayout.Bounds;
import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.ui.layout.UILayout.Insets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
public final class FlowFunction implements UILayout.Function {

    public static FlowFunction of() {
        return new FlowFunction();
    }

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
     * Constructs a new <code>FlowLayout</code> with a centered alignment and a
     * default 5-unit horizontal and vertical gap.
     */
    private FlowFunction() {
        this(5, 5);
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
    private FlowFunction(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
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
 * @return actual row height
 */
Map<Component, Bounds> boundsFor(
    final Collection<Component> components, Bounds within)
{
          int      x = within.x;
          int      y = within.y;
    final int height = within.height;
    Map<Component, Bounds> bounds = Maps.newHashMap();
    // for every visible component
    for (Component m : components) {
        Dimension size = m.getPreferredSize();
        bounds.put(m, Bounds.of(x,y,size));
        y = y + (height - size.height) / 2;
        x += size.width + hgap;
    }
    return bounds;
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
public ImmutableMap<Component, Bounds> layout(
    ImmutableList<Component> components, ImmutableMap<Component, Constraint> constraints, Insets insets, Dimension dim) {
    Map<Component,Bounds> bounds = Maps.newHashMap();
    final int maxwidth = dim.width - (insets.left + insets.right + hgap*2);
    int x = 0;
    int y = insets.top + vgap;
    int rowh = 0;

    List<Component> row = Lists.newArrayList();
    // for every visible component
    for (Component m : components) {
        Dimension d = m.getPreferredSize();
        row.add(m);
        x += d.width + hgap;
        // if there is no more space on this row
        if (x >= maxwidth) {
            Bounds within = Bounds.of(insets.left + hgap, y,  maxwidth - x, rowh);
            bounds.putAll(boundsFor(row.subList(0, row.size()-1),within));
            row.clear();
            row.add(m);
            x = d.width + hgap;
            y += vgap + rowh;
            rowh = d.height;
        } else {
            rowh = Math.max(rowh, d.height);
        }
    }
    Bounds within = Bounds.of(insets.left + hgap, y,  maxwidth - x, rowh);
    bounds.putAll(boundsFor(row,within));
    return ImmutableMap.copyOf(bounds);
}

    /**
     * Returns a string representation of this <code>FlowLayout</code>
     * object and its values.
     * @return     a string representation of this layout
     */
    @Override
    public String toString() {
        String str = "";
        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + str + "]";
    }

    public static void main( String[] args ) {
		SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                    test();
                }
            }
        );
	}

	static void test() {
     	JFrame frame = new JFrame();
		JPanel panel = new JPanel();
        panel.setLayout(AwtLayoutAdapter.of(FunctionalLayoutManager.of(FlowFunction.of())));
		for ( int k = 0; k < 120; k++ ) {
			panel.add( new JButton( "Button"  + k ) );
        }
		frame.add(panel);
		frame.setSize( 300, 300 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );
	}
}

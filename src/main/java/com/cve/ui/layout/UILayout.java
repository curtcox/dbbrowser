package com.cve.ui.layout;

import com.google.common.collect.ImmutableList;

/**
 *
 * @author Curt
 */
public interface UILayout {

    interface Manager {

        void addLayoutComponent(String name, Component comp);

        void removeLayoutComponent(Component comp);

        Dimension preferredLayoutSize(Container parent);

        Dimension minimumLayoutSize(Container parent);

        void layoutContainer(Container parent);
    }

    interface Component {

        boolean isVisible();

        Dimension getPreferredSize();

        int getBaseline(int width, int height);

        void setSize(int width, int height);

        Dimension getMinimumSize();

        void setLocation(int x, int cy);
        void setBounds(int x, int y, int width, int height);

        int getHeight();

        int getWidth();
    }

    interface Container {

        Object getTreeLock();

        int getWidth();
        int getHeight();

        Insets getInsets();

        ImmutableList<Component> getComponents();

        Orientation getComponentOrientation();
    }

    final class Orientation {

        final boolean leftToRight;

        private Orientation(boolean leftToRight) {
            this.leftToRight = leftToRight;
        }

        static Orientation of(boolean leftToRight) {
            return new Orientation(leftToRight);
        }
    }

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

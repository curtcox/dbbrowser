package com.cve.ui.layout;

import com.cve.ui.layout.UILayout.Component;
import com.cve.ui.layout.UILayout.Constraint;
import com.cve.ui.layout.UILayout.Container;
import com.cve.ui.layout.UILayout.Dimension;
import com.cve.ui.layout.UILayout.Insets;

/**
 Grid Layout which allows components of differrent sizes
*/
public class GridLayout2 implements UILayout.Manager {

    final int rows;
    final int cols;
    final int hgap;
    final int vgap;

  private GridLayout2() {
    this(1, 0, 0, 0);
  }

  private GridLayout2(int rows, int cols) {
    this(rows, cols, 0, 0);
  }

  private GridLayout2(int rows, int cols, int hgap, int vgap) {
    this.rows = rows;
    this.cols = cols;
    this.hgap = hgap;
    this.vgap = vgap;
  }

    static GridLayout2 of(int rows, int cols, int hgap, int vgap) {
        return new GridLayout2(rows,cols,hgap,vgap);
    }
  @Override
  public Dimension preferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        int ncomponents = parent.getComponents().size();
        int nrows = rows;
        int ncols = cols;
        if (nrows > 0) {
            ncols = (ncomponents + nrows - 1) / nrows;
        } else {
            nrows = (ncomponents + ncols - 1) / ncols;
        }
        int[] w = new int[ncols];
        int[] h = new int[nrows];
        for (int i = 0; i < ncomponents; i ++) {
            int r = i / ncols;
            int c = i % ncols;
            Component comp = parent.getComponents().get(i);
            Dimension d = comp.getPreferredSize();
            if (w[c] < d.width) {
              w[c] = d.width;
            }
            if (h[r] < d.height) {
              h[r] = d.height;
            }
          }
          int nw = 0;
          for (int j = 0; j < ncols; j ++) {
            nw += w[j];
          }
          int nh = 0;
          for (int i = 0; i < nrows; i ++) {
            nh += h[i];
          }
          return Dimension.of(insets.left + insets.right + nw + (ncols-1)*hgap,
              insets.top + insets.bottom + nh + (nrows-1)*vgap);
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
      Insets insets = parent.getInsets();
      int ncomponents = parent.getComponents().size();
      int nrows = rows;
      int ncols = cols;
      if (nrows > 0) {
        ncols = (ncomponents + nrows - 1) / nrows;
      }
      else {
        nrows = (ncomponents + ncols - 1) / ncols;
      }
      int[] w = new int[ncols];
      int[] h = new int[nrows];
      for (int i = 0; i < ncomponents; i ++) {
        int r = i / ncols;
        int c = i % ncols;
        Component comp = parent.getComponents().get(i);
        Dimension d = comp.getMinimumSize();
        if (w[c] < d.width) {
          w[c] = d.width;
        }
        if (h[r] < d.height) {
          h[r] = d.height;
        }
      }
      int nw = 0;
      for (int j = 0; j < ncols; j ++) {
        nw += w[j];
      }
      int nh = 0;
      for (int i = 0; i < nrows; i ++) {
        nh += h[i];
      }
      return Dimension.of(insets.left + insets.right + nw + (ncols-1)*hgap,
          insets.top + insets.bottom + nh + (nrows-1)*vgap);
  }

    @Override
  public void layoutContainer(Container parent) {
      Insets insets = parent.getInsets();
      int ncomponents = parent.getComponents().size();
      int nrows = rows;
      int ncols = cols;
      if (ncomponents == 0) {
        return;
      }
      if (nrows > 0) {
        ncols = (ncomponents + nrows - 1) / nrows;
      }
      else {
        nrows = (ncomponents + ncols - 1) / ncols;
      }
	  // scaling factors
      Dimension pd = preferredLayoutSize(parent);
      double sw = (1.0 * parent.getSize().width) / pd.width;
      double sh = (1.0 * parent.getSize().height) / pd.height;
      // scale
      int[] w = new int[ncols];
      int[] h = new int[nrows];
      for (int i = 0; i < ncomponents; i ++) {
        int r = i / ncols;
        int c = i % ncols;
        Component comp = parent.getComponents().get(i);
        int width  = comp.getPreferredSize().width;
        int height = comp.getPreferredSize().height;
        width = (int) (sw * width);
        height = (int) (sh * height);
        if (w[c] < width) {
          w[c] = width;
        }
        if (h[r] < height) {
          h[r] = height;
        }
      }
      for (int c = 0, x = insets.left; c < ncols; c ++) {
        for (int r = 0, y = insets.top; r < nrows; r ++) {
          int i = r * ncols + c;
          if (i < ncomponents) {
            parent.getComponents().get(i).setBounds(x, y, w[c], h[r]);
          }
          y += h[r] + vgap;
        }
        x += w[c] + hgap;
      }
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
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
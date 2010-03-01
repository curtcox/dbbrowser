package com.cve.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * See "JScrollPane and FlowLayout do not interact properly"
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5082531
 * "Swing - Use scrollbars when panel too small for components in it [Locked]"
 * http://forums.sun.com/thread.jspa?forumID=57&threadID=701797&start=2
 * @author curt
 */
final class JScrollableFlowPanel extends JPanel implements Scrollable {

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, getParent().getWidth(), height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getPreferredHeight());
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        int hundredth = (orientation == SwingConstants.VERTICAL ? getParent().getHeight() : getParent().getWidth()) / 100;
        return hundredth == 0 ? 1 : hundredth;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation == SwingConstants.VERTICAL ? getParent().getHeight() : getParent().getWidth();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    private int getPreferredHeight() {
        int rv = 0;
        for (int k = 0, count = getComponentCount(); k < count; k++) {
            Component comp = getComponent(k);
            Rectangle r = comp.getBounds();
            int height = r.y + r.height;
            if (height > rv) {
                rv = height;
            }
        }
        rv += ((FlowLayout) getLayout()).getVgap();
        return rv;
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
		JScrollableFlowPanel panel = new JScrollableFlowPanel();
		for ( int k = 0; k < 120; k++ ) {
			panel.add( new JButton( "Button"  + k ) );
        }
		JScrollPane scroll = new JScrollPane( panel );
		frame.getContentPane().setLayout( new BorderLayout() );
		frame.getContentPane().add( scroll, BorderLayout.CENTER );
		frame.setSize( 300, 300 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );
	}

}

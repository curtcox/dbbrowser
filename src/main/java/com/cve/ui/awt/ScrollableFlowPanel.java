package com.cve.ui.awt;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.ScrollPane;

/**
 * See "JScrollPane and FlowLayout do not interact properly"
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5082531
 * "Swing - Use scrollbars when panel too small for components in it [Locked]"
 * http://forums.sun.com/thread.jspa?forumID=57&threadID=701797&start=2
 * @author curt
 */
final class ScrollableFlowPanel extends Panel {

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, getParent().getWidth(), height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getPreferredHeight());
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
		EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                    test();
                }
            }
        );
	}

	static void test() {
     	Frame frame = new Frame();
		ScrollableFlowPanel panel = new ScrollableFlowPanel();
		for ( int k = 0; k < 120; k++ ) {
			panel.add( new Button( "Button"  + k ) );
        }
		ScrollPane scroll = new ScrollPane();
        scroll.add(panel);
		frame.setLayout( new BorderLayout() );
		frame.add( scroll, BorderLayout.CENTER );
		frame.setSize( 300, 300 );
		AwtCloser.exitOnClose(frame);
		frame.setVisible( true );
	}

}

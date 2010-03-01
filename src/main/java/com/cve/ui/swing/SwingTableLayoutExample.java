package com.cve.ui.swing;

import com.cve.ui.layout.AwtLayoutAdapter;
import com.cve.ui.layout.TableLayout;
import com.cve.ui.layout.TableLayoutConstraints;
import com.cve.ui.layout.TableLayoutConstants;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * This is an example of TableLayout in action for Swing.
 */
final class SwingTableLayoutExample {

    public static void main (String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                showFrame();
            }
        });
    }

    public static void showFrame() {
        // Create a frame
        JFrame frame = new JFrame("Example of TableLayout");
        frame.setBounds (100, 100, 300, 300);

        // Create a TableLayout for the frame
        double border = 10;
        double[] cols = {border, 0.10, 20, TableLayoutConstants.FILL, 20, 0.20, border};
        double[] rows = {border, 0.20, 20, TableLayoutConstants.FILL, 20, 0.20, border};

        frame.setLayout (AwtLayoutAdapter.of(TableLayout.of(cols,rows)));

/*
Justification
A component in a single cell can be justified both horizontally and vertically.
For each orientation there are four justifications.

For horizontal justification there are left, center, right, and full.  (L C R F)
The default justification is full, meaning that the component's width will match
the cell's width. The other justifications have an effect only if the component's
preferred width is less than the cell's width. The four justifications in
TableLayout are similar to the four justifications used in word processors for
paragraphs.

The component can also be justified vertically.
The four posibilites are top, center, bottom, and full.  (T C B F)
The behavior is analogous to the horizontal justification.

To justify a component, simply specify the first letter of the desired justification,
e.g., 'l' for left.
*/
        // Add buttons
        TableLayout.Justification C = TableLayout.Justification.CENTER;
        frame.add (new JButton("cell 1,1 to cell 5,1"),      TableLayoutConstraints.of(1, 1, 5, 1));
        frame.add (new JButton("cell 1,5 to cell 5,5"),      TableLayoutConstraints.of(1, 5, 5, 5));
        frame.add (new JButton("Cell 1,3"),                  TableLayoutConstraints.of(1, 3));
        frame.add (new JButton("Cell 5,3"),                  TableLayoutConstraints.of(5, 3));
        frame.add (new JButton("cell 3,3 center, center"),   TableLayoutConstraints.of(3, 3, C, C));
        frame.add (new JButton("cell 3,3 to cell 3,5"),      TableLayoutConstraints.of(3, 3, 3, 5));
        // Allow user to close the window to terminate the program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show frame
        frame.show();
    }



}

package com.cve.ui.layout;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Test extends JFrame {

    public Test(LayoutManager layout, JComponent[] components) {
        super(layout.getClass().getName());
        JPanel panel = new JPanel(layout);
        //--- code needed to add the components
        // less than using a GridBagLayout
        for (JComponent component : components) {
            panel.add(component);
        }
        //---
        panel.setBorder(new EtchedBorder());
        setContentPane(new JScrollPane(panel));
        pack();
        show();
    }


    public Test(int columns, Insets insets, JComponent[] components) {
        super(GridBagLayout.class.getName());
        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel(layout);
        //--- code needed to add the components
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = insets;
        constraints.fill = GridBagConstraints.BOTH;
        int i = 0;
        for (JComponent component : components) {
            constraints.gridx = i % columns;
	    constraints.gridy = i / columns;
	    layout.setConstraints(component, constraints);
            panel.add(component);
            i++;
        }
        //---
        panel.setBorder(new EtchedBorder());
        setContentPane(panel);
        pack();
        show();
    }

    // Generate n components of random sizes
    public static JComponent[] generateRandomComponents(int n) {
        Random r = new Random(0);
        JComponent[] c = new JComponent[n];
        int m = n;
        while (m > 0) {
                int i = r.nextInt(n);
                if (c[i] == null) {
                        c[i] = new JLabel("Component " + i, null, SwingConstants.CENTER);
                        int w = 5 * (2 + r.nextInt(20));
                        int h = 5 * (2 + r.nextInt(20));
                        c[i].setPreferredSize(new Dimension(w, h));
                        c[i].setBorder(new EtchedBorder());
                        m --;
                }
        }
        return c;
    }

    // Generate the components for the "change-password" panel
    public static JComponent[] generateChangePasswordComponents() {
        JComponent[] c = new JComponent[6];
        c[0] = new JLabel("Login");
        c[1] = new JTextField("", 20);
        c[2] = new JLabel("Password");
        c[3] = new JPasswordField("", 20);
        c[4] = new JLabel("Re-enter Password");
        c[5] = new JPasswordField("", 20);
        return c;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                // int rows, int cols, int hgap, int vgap
                new Test(AwtLayoutAdapter.of(GridLayout2.of(40, 10, 4, 2)), generateRandomComponents(400));
                //new Test(new GridLayout(4, 4, 4, 2), generateRandomComponents(14));
                //new Test(4, new Insets(1, 2, 1, 2), generateRandomComponents(14));

                //new Test(new GridLayout(3, 2, 2, 2), generateChangePasswordComponents());
                //new Test(new GridLayout2(3, 2, 2, 2), generateChangePasswordComponents());
                //new Test(2, new Insets(1, 1, 1, 1), generateChangePasswordComponents());
            }
        });
    }
}
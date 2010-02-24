package com.cve.ui.awt;

import com.cve.ui.UIConstructor;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UIPage;
import com.cve.ui.UITable;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Constructs an AWT UI from toolkit-independent UIElements.
 * @author curt
 */
final class AwtUIConstructor implements UIConstructor {

    private AwtUIConstructor() {}

    static AwtUIConstructor of() {
        return new AwtUIConstructor();
    }

    @Override
    public Component construct(UIElement e) {
        if (e instanceof UILabel) { return label((UILabel) e);  }
        if (e instanceof UIPage)  { return page((UIPage) e);   }
        String message = "Unsupported element " + e.getClass();
        throw new IllegalArgumentException(message);
    }

    @Override
    public Label label(UILabel label) {
        return new Label(label.value);
    }

    @Override
    public Panel page(UIPage page) {
        Panel panel = new Panel();
        for (UIElement element : page.items) {
            panel.add(construct(element));
        }
        return panel;
    }

    @Override
    public Panel table(UITable table) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                showAllElements();
            }
        });
    }

    static void showAllElements() {
        Frame frame = new Frame();
        Panel panel = new Panel();
        frame.add(panel);
        // Allow user to close the window to terminate the program
        frame.addWindowListener(new WindowAdapter() {
            @Override
                public void windowClosing (WindowEvent e) {
                    System.exit (0);
                }
            }
        );
        AwtUIConstructor constructor = AwtUIConstructor.of();
        UIElement ui = UIPage.of(
            UILabel.of("Label")
        );
        panel.add(constructor.construct(ui));
        frame.setVisible(true);
        frame.setSize(800,800);
    }

}

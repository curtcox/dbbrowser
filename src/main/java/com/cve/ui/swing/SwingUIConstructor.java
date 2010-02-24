package com.cve.ui.swing;

import com.cve.ui.UIElement;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 *
 * @author curt
 */
final class SwingUIConstructor {

    private SwingUIConstructor() {}

    static SwingUIConstructor of() {
        return new SwingUIConstructor();
    }

    JComponent construct(UIElement e) {
        return new JButton(e.toString());
    }
}

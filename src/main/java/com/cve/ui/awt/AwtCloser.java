package com.cve.ui.awt;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author curt
 */
final class AwtCloser {

    static void exitOnClose(Window window) {
        window.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing (WindowEvent e) {
                    System.exit (0);
                }
            }
        );
    }
}

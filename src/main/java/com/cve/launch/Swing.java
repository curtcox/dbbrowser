package com.cve.launch;

import com.cve.lang.URIObject;
import com.cve.ui.swing.SwingRouterFrame;
import com.cve.util.Check;
import com.cve.web.core.WebApp;


/**
 * For dealing with the Swing UI.
 * @author curt
 */
final class Swing {

    final SwingRouterFrame frame;

    private Swing(SwingRouterFrame frame) {
        this.frame = Check.notNull(frame);
    }

    static Swing start(WebApp webApp) {
        return new Swing(SwingRouterFrame.of(webApp));
    }

    void browse(URIObject uri) {
        frame.browse(uri);
    }

}

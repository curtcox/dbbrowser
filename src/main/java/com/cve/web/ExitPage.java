package com.cve.web;

import com.cve.log.Log;
import com.cve.ui.UIForm;
import com.cve.ui.UISubmit;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;

/**
 * The "Are you sure you want to exit?" page.
 * @author Curt
 */
final class ExitPage implements Model {

    final Log log;

    final UIForm exitForm;

    private ExitPage(Log log) {
        this.log = notNull(log);
        exitForm = UIForm.postAction(
            URIs.of("/exit"),log)
            .with(UISubmit.value("exit"));
    }

    public static ExitPage of(Log log) {
        return new ExitPage(log);
    }
}

package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIForm;
import com.cve.ui.UISubmit;
import com.cve.util.URIs;

/**
 * The "Are you sure you want to exit?" page.
 * @author Curt
 */
final class ExitPage implements Model {

    final Log log = Logs.of();

    final UIForm exitForm;

    private ExitPage() {
        
        exitForm = UIForm.postAction(
            URIs.of("/exit"))
            .with(UISubmit.value("exit"));
    }

    public static ExitPage of() {
        return new ExitPage();
    }
}

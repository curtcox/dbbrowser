package com.cve.web.core.models;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIForm;
import com.cve.ui.UISubmit;
import com.cve.util.URIs;
import com.cve.web.core.Model;

/**
 * The "Are you sure you want to exit?" page.
 * @author Curt
 */
public final class ExitPage implements Model {

    final Log log = Logs.of();

    public final UIForm exitForm;

    private ExitPage() {
        
        exitForm = UIForm.postAction(
            URIs.of("/exit"))
            .with(UISubmit.value("exit"));
    }

    public static ExitPage of() {
        return new ExitPage();
    }
}

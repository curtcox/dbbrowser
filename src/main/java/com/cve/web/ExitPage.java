package com.cve.web;

import com.cve.ui.UIForm;
import com.cve.ui.UISubmit;
import com.cve.util.URIs;

/**
 * The "Are you sure you want to exit?" page.
 * @author Curt
 */
final class ExitPage implements Model {
    final UIForm exitForm = UIForm.postAction(URIs.of("/exit"))
        .with(UISubmit.value("exit"));
}

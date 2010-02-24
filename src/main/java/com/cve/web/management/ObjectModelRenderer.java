package com.cve.web.management;

import com.cve.web.management.browsers.ObjectBrowser;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import javax.annotation.concurrent.Immutable;

/**
 * For rendering ObjectModels to HTML.
 * @author Curt
 */
@Immutable
final class ObjectModelRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    /**
     * Use the factory
     */
    private ObjectModelRenderer() {}

    public static ObjectModelRenderer of() {
        return new ObjectModelRenderer();
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        ObjectModel objectModel = (ObjectModel) model;
        Object o = objectModel.object;
        return render(o);
    }

    UIElement render(Object o) {
        return ObjectBrowser.of(o).toHTML();
    }
}

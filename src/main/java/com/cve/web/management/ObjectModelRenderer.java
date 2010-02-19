package com.cve.web.management;

import com.cve.web.management.browsers.ObjectBrowser;
import com.cve.ui.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
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
    public HtmlPage render(Model model, ClientInfo client) {
        ObjectModel objectModel = (ObjectModel) model;
        Object o = objectModel.object;
        return HtmlPage.guts(render(o));
    }

    String render(Object o) {
        HTMLTags tags = HTMLTags.of();
        return tags.html(tags.body(ObjectBrowser.of(o).toHTML()));
    }
}

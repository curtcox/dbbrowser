package com.cve.web.log;

import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import static com.cve.util.Check.notNull;

/**
 *
 * @author Curt
 */
final class ObjectModelRenderer implements ModelHtmlRenderer {

    final Log log;

    private ObjectModelRenderer(Log log) {
        this.log = notNull(log);
    }

    public static ObjectModelRenderer of(Log log) {
        return new ObjectModelRenderer(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ObjectModel objectModel = (ObjectModel) model;
        Object o = objectModel.object;
        return HtmlPage.guts(render(o));
    }

    String render(Object o) {
        HTMLTags tags = HTMLTags.of(log);
        return tags.html(tags.body(new ObjectBrowser(o).toHTML()));
    }
}

package com.cve.web.log;

import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import static com.cve.html.HTML.*;

/**
 *
 * @author Curt
 */
final class ObjectModelRenderer implements ModelHtmlRenderer {

    ObjectModelRenderer() {}

    @Override
    public String render(Model model, ClientInfo client) {
        ObjectModel objectModel = (ObjectModel) model;
        Object o = objectModel.object;
        return render(o);
    }

    String render(Object o) {
        return html(body(new ObjectBrowser(o).toHTML()));
    }
}
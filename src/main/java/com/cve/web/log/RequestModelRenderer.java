package com.cve.web.log;

import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;

/**
 *
 * @author Curt
 */
final class RequestModelRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private RequestModelRenderer() {
        
    }

    public static RequestModelRenderer of() {
        return new RequestModelRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        RequestModel requestModel = (RequestModel) model;
        return HtmlPage.guts(render(requestModel));
    }

    String render(RequestModel model) {
        HTMLTags tags = HTMLTags.of();
        return tags.html(tags.body(new ObjectBrowser(model).toHTML()));
    }
}

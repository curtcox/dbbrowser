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
final class RequestIndexRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private RequestIndexRenderer() {
        
    }

    public static RequestIndexRenderer of() {
        return new RequestIndexRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        RequestIndexModel requestModel = (RequestIndexModel) model;
        return HtmlPage.guts(render(requestModel));
    }

    String render(RequestIndexModel model) {
        HTMLTags tags = HTMLTags.of();
        return tags.html(tags.body(new ObjectBrowser(model).toHTML()));
    }
}

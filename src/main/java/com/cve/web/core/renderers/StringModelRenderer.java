package com.cve.web.core.renderers;

import com.cve.web.core.ModelHtmlRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.models.StringModel;

/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class StringModelRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private StringModelRenderer() {
        
    }

    public static StringModelRenderer of() {
        return new StringModelRenderer();
    }
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        StringModel objectModel = (StringModel) model;
        String s = objectModel.string;
        return HtmlPage.guts(s);
    }

}

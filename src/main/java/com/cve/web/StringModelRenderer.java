package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;

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

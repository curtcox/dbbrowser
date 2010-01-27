package com.cve.web;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;

/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class StringModelRenderer implements ModelHtmlRenderer {

    final Log log;

    private StringModelRenderer(Log log) {
        this.log = notNull(log);
    }

    public static StringModelRenderer of(Log log) {
        return new StringModelRenderer(log);
    }
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        StringModel objectModel = (StringModel) model;
        String s = objectModel.string;
        return HtmlPage.guts(s,log);
    }

}

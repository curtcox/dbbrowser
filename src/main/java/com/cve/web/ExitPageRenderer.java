package com.cve.web;

import com.cve.log.Log;
import com.cve.util.URIs;
import java.net.URI;
import static com.cve.util.Check.notNull;
/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class ExitPageRenderer implements ModelHtmlRenderer {

    final Log log;

    private static URI HELP = URIs.of("/resource/help/Exit.html");

    private ExitPageRenderer(Log log) {
        this.log = notNull(log);
    }

    public static ExitPageRenderer of(Log log) {
        return new ExitPageRenderer(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ExitPage page = (ExitPage) model;
        return HtmlPage.gutsHelp(render(page),HELP,log);
    }

    private String render(ExitPage page) {
        return page.exitForm.toString();
    }
}

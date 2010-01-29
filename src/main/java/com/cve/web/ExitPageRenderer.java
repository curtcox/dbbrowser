package com.cve.web;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.URIs;
import java.net.URI;
/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class ExitPageRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private static URI HELP = URIs.of("/resource/help/Exit.html");

    private ExitPageRenderer() {
        
    }

    public static ExitPageRenderer of() {
        return new ExitPageRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ExitPage page = (ExitPage) model;
        return HtmlPage.gutsHelp(render(page),HELP);
    }

    private String render(ExitPage page) {
        return page.exitForm.toString();
    }
}

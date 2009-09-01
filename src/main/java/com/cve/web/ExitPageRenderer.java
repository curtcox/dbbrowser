package com.cve.web;

import com.cve.util.URIs;
import java.net.URI;

/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class ExitPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resources/help/Exit.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        ExitPage page = (ExitPage) model;
        return HtmlPage.gutsHelp(render(page),HELP);
    }

    private String render(ExitPage page) {
        return page.exitForm.toString();
    }
}

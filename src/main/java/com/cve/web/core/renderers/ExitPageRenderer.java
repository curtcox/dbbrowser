package com.cve.web.core.renderers;

import com.cve.lang.URIObject;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import com.cve.util.URIs;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.models.ExitPage;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;

/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class ExitPageRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();

    private static URIObject HELP = URIs.of("/resource/help/Exit.html");

    private ExitPageRenderer() {
        
    }

    public static ExitPageRenderer of() {
        return new ExitPageRenderer();
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        ExitPage page = (ExitPage) model;
        return HtmlPage.gutsHelp(render(page),HELP);
    }

    private String render(ExitPage page) {
        return page.exitForm.toString();
    }
}

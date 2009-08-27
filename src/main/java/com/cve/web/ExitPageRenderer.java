package com.cve.web;

/**
 * For rendering strings to HTML.
 * @author curt
 */
public final class ExitPageRenderer implements ModelHtmlRenderer {

    public String render(Model model, ClientInfo client) {
        ExitPage page = (ExitPage) model;
        return render(page);
    }

    private String render(ExitPage page) {
        return page.exitForm.toString();
    }
}
package com.cve.web;

import com.cve.util.Throwables;

/**
 * For rendering throwables to HTML.
 * @author curt
 */
public final class ThrowableModelRenderer implements ModelHtmlRenderer {

    public String render(Model model, ClientInfo client) {
        ThrowableModel objectModel = (ThrowableModel) model;
        Throwable t = objectModel.getThrowable();
        return render(t);
    }

    String render(Throwable t) {
        return Throwables.toHtml(t);
    }
}

package com.cve.web;

import com.cve.html.HTML;
import com.cve.util.Throwables;

/**
 * For rendering throwables to HTML.
 * @author curt
 */
public final class ThrowableModelRenderer implements ModelRenderer {

    public Object render(Model model, ClientInfo client) {
        ThrowableModel objectModel = (ThrowableModel) model;
        Throwable t = objectModel.getThrowable();
        return render(t);
    }

    HTML render(Throwable t) {
        return HTML.of(Throwables.toHtml(t));
    }
}

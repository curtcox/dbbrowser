package com.cve.web.db;

import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelRenderer;

/**
 *
 * @author Curt
 */
final class AddServerPageRenderer implements ModelRenderer {

    AddServerPageRenderer() {}

    public Object render(Model model, ClientInfo client) {
        AddServerPage page = (AddServerPage) model;
        return render(page);
    }

    private String render(AddServerPage page) {
        return page.toString();
    }
}

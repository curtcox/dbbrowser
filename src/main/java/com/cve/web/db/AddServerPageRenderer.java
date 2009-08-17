package com.cve.web.db;

import com.cve.ui.UIForm;
import com.cve.ui.UILabel;
import com.cve.ui.UISubmit;
import com.cve.ui.UIText;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;

import static com.cve.web.db.AddServerPage.*;
/**
 * For adding servers.
 * @author Curt
 */
final class AddServerPageRenderer implements ModelHtmlRenderer {

    AddServerPageRenderer() {}

    public String render(Model model, ClientInfo client) {
        AddServerPage page = (AddServerPage) model;
        return render(page);
    }

    private String render(AddServerPage page) {
        UIForm addServer = UIForm.postAction(URIs.of("add"))
            .with(UILabel.value(page.message))
            .with(UISubmit.value("add"))
            .with(UIText.nameValue(SERVER,   page.server.toString()))
            .with(UIText.nameValue(URL,      page.jdbcurl))
            .with(UIText.nameValue(USER,     page.user))
            .with(UIText.nameValue(PASSWORD, page.password))
        ;
        return addServer.toString();
    }
}

package com.cve.web.db;

import com.cve.ui.UIForm;
import com.cve.ui.UITable;
import static com.cve.ui.UIBuilder.*;
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
        UITable table = table(
             row(detail(page.message)),
             row(label(SERVER),   text(SERVER,   page.server.toString()) ),
             row(label(URL),      text(URL,      page.jdbcurl          ) ),
             row(label(USER),     text(USER,     page.user             ) ),
             row(label(PASSWORD), text(PASSWORD, page.password         ) ),
             row(submit("add"))
        );
        UIForm addServer = UIForm.postAction(URIs.of("add")).with(table);
        return addServer.toString();
    }


}

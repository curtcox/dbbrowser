package com.cve.web.db;

import com.cve.db.dbio.DBDriver;
import com.cve.html.HTML;
import com.cve.ui.UIForm;
import com.cve.ui.UITable;
import static com.cve.ui.UIBuilder.*;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import static com.cve.log.Log.args;
import static com.cve.web.db.AddServerPage.*;
/**
 * For adding servers.
 * @author Curt
 */
final class AddServerPageRenderer implements ModelHtmlRenderer {

    AddServerPageRenderer() {}

    @Override
    public String render(Model model, ClientInfo client) {
        args(model,client);
        return render((AddServerPage) model);
    }

    private String render(AddServerPage page) {
        UITable table = table(
             row(label(SERVER),   text(SERVER,   page.server.toString()) ),
             row(label(URL),      text(URL,      page.jdbcurl          ) ),
             row(label(USER),     text(USER,     page.user             ) ),
             row(label(PASSWORD), text(PASSWORD, page.password         ) ),
             row(submit("add"))
        );
        UIForm addServer = UIForm.postAction(URIs.of("add"))
            .with(label(page.message))
            .with(table)
            .with(label(supportedFormats()));
        return addServer.toString();
    }

    static String supportedFormats() {
        StringBuilder out = new StringBuilder();
        for (DBDriver driver : DBDriver.values()) {
            out.append(HTML.li(driver.getJDBCURL("server").toString()));
        }
        return "Available URL formats " + HTML.ol(out.toString());
    }

}

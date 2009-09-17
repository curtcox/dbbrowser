package com.cve.web.db.servers;

import com.cve.db.dbio.driver.DBDriver;
import com.cve.html.HTML;
import com.cve.ui.UIForm;
import com.cve.ui.UITable;
import static com.cve.ui.UIBuilder.*;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import java.net.URI;
import static com.cve.log.Log.args;
import static com.cve.web.db.servers.AddServerPage.*;
/**
 * For adding servers.
 * @author Curt
 */
final class AddServerPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/AddServer.html");

    AddServerPageRenderer() {}

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        String title = "Add a Server";
        String guts = render((AddServerPage) model);
        String[] nav = new String[0];
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
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

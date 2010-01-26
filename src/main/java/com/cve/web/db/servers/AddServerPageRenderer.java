package com.cve.web.db.servers;

import com.cve.html.HTMLTags;
import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.ui.UIForm;
import com.cve.ui.UITable;
import com.cve.ui.UIBuilder;
import com.cve.ui.UIDetail;
import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.ui.UIRow;
import com.cve.ui.UISubmit;
import com.cve.ui.UIText;
import com.cve.util.URIs;
import com.cve.web.ClientInfo;
import com.cve.web.HtmlPage;
import com.cve.web.Model;
import com.cve.web.ModelHtmlRenderer;
import java.net.URI;
import static com.cve.web.db.servers.AddServerPage.*;
import static com.cve.util.Check.notNull;
/**
 * For adding servers.
 * @author Curt
 */
final class AddServerPageRenderer implements ModelHtmlRenderer {

    final HTMLTags tags;

    final UIBuilder builder;

    final Log log;

    private static URI HELP = URIs.of("/resource/help/AddServer.html");

    private AddServerPageRenderer(Log log) {
        this.log = notNull(log);
        this.tags = HTMLTags.of(log);
        this.builder = UIBuilder.of(log);
    }

    UIRow     row(UIDetail... details)     { return builder.row(details);  }
    UIRow     row(UIElement... details)    { return builder.row(details);  }
    UILabel label(String value)            { return builder.label(value);  }
    UIText text(String name, String value) { return builder.text(name,value); }
    UITable table(UIRow... rows)           { return builder.table(rows); }
    UISubmit submit(String value)          { return builder.submit(value); }

    static AddServerPageRenderer of(Log log) {
        return new AddServerPageRenderer(log);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.notNullArgs(model,client);
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
        UIForm addServer = UIForm.postAction(URIs.of("add"),log)
            .with(label(page.message))
            .with(table)
            .with(label(supportedFormats()));
        return addServer.toString();
    }

    String supportedFormats() {
        StringBuilder out = new StringBuilder();
        for (DBDriver driver : DBDriver.values()) {
            out.append(tags.li(driver.getJDBCURL("server").toString()));
        }
        return "Available URL formats " + tags.ol(out.toString());
    }

}

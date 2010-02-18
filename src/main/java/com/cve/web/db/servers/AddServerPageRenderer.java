package com.cve.web.db.servers;

import com.cve.html.HTMLTags;
import com.cve.io.db.driver.DBDrivers;
import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
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
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
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

    final Log log = Logs.of();

    final ManagedFunction.Factory managedFunction;

    final DBServersStore serversStore;

    private static URI HELP = URIs.of("/resource/help/AddServer.html");

    private AddServerPageRenderer(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        this.tags    = HTMLTags.of();
        this.builder = UIBuilder.of();
    }

    UIRow     row(UIDetail... details)     { return builder.row(details);  }
    UIRow     row(UIElement... details)    { return builder.row(details);  }
    UILabel label(String value)            { return builder.label(value);  }
    UIText text(String name, String value) { return builder.text(name,value); }
    UITable table(UIRow... rows)           { return builder.table(rows); }
    UISubmit submit(String value)          { return builder.submit(value); }

    static AddServerPageRenderer of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return new AddServerPageRenderer(managedFunction,serversStore);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
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

    String supportedFormats() {
        StringBuilder out = new StringBuilder();
        for (DBDriver driver : DBDrivers.of(managedFunction,serversStore).values()) {
            out.append(tags.li(driver.getJDBCURL("server").toString()));
        }
        return "Available URL formats " + tags.ol(out.toString());
    }

}

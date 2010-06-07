package com.cve.web.db.databases;

import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.html.CSS;
import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIElement;
import static com.cve.ui.UIBuilder.*;
import com.cve.ui.UITableBuilder;
import com.cve.util.Replace;

import com.cve.util.URIs;
import com.cve.web.db.NavigationButtons;

/**
 * Renders a DatabasePage to a HTML string.
 */
final class DatabasesPageRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();
    private final NavigationButtons buttons;

    private static URIObject HELP = URIs.of("/resource/help/Databases.html");

    private DatabasesPageRenderer() {
        buttons = NavigationButtons.of();
    }

    public static DatabasesPageRenderer of() {
        return new DatabasesPageRenderer();
    }
    
    @Override
    public UIElement render(Model model, ClientInfo client) {
        DatabasesPage page = (DatabasesPage) model;
        DBServer server = page.server;
        String title = "Available Databases on " + server.toString();
        String guts  = tableOfDatabases(page);
        String nav[] = new String[] {
            Replace.bracketQuote("Available Databases on <a href=[/]>server</a> ") + server,
            buttons.SEARCH
        };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfDatabases(DatabasesPage page) {
        UITableBuilder out = UITableBuilder.of();
        out.add(row(header("Database"),header("Tables")));
        for (Database database : page.databases) {
            out.add(
                row(
                    detail(database.linkTo().toString(), CSS.DATABASE),
                    detail(tablesOn(page,database),      CSS.TABLE)
                )
            );
        }
        return out.build().toString();
    }

    String tablesOn(DatabasesPage page, Database database) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (DBTable table : page.tables.get(database)) {
            out.append(table.linkTo() + " ");
            i++;
            if (i>20) {
                out.append("...");
                return out.toString();
            }
        }
        return out.toString();
    }


}

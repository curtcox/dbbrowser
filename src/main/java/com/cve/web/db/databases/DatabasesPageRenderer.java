package com.cve.web.db.databases;

import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Replace;

import com.cve.util.URIs;
import com.cve.web.db.NavigationButtons;
import java.net.URI;
/**
 * Renders a DatabasePage to a HTML string.
 */
final class DatabasesPageRenderer implements ModelHtmlRenderer {

    final Log log = Logs.of();
    private final NavigationButtons buttons;
    private final HTMLTags tags;

    private static URI HELP = URIs.of("/resource/help/Databases.html");

    String tr(String s) { return tags.tr(s); }
    String td(String s, CSS css) { return tags.td(s,css); }
    String h1(String s) { return tags.h1(s); }
    String h2(String s) { return tags.h2(s); }
    String td(String s) { return tags.td(s); }
    String th(String s) { return tags.th(s); }
    String borderTable(String s) { return tags.borderTable(s); }

    private DatabasesPageRenderer() {
        
        buttons = NavigationButtons.of();
        tags = HTMLTags.of();
    }

    public static DatabasesPageRenderer of() {
        return new DatabasesPageRenderer();
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
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
        StringBuilder out = new StringBuilder();
        out.append(th("Database") + th("Tables"));
        for (Database database : page.databases) {
            out.append(
                tr(
                    td(database.linkTo().toString(), CSS.DATABASE) +
                    td(tablesOn(page,database),      CSS.TABLE)
                )
            );
        }
        return borderTable(out.toString());
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

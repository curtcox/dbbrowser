package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.html.CSS;
import com.cve.util.Replace;

import com.cve.util.URIs;
import java.net.URI;
import static com.cve.html.HTML.*;

/**
 * Renders a DatabasePage to a HTML string.
 */
public final class DatabasesPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resources/help/Databases.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        DatabasesPage page = (DatabasesPage) model;
        Server server = page.server;
        String title = "Available Databases on " + server.toString();
        String guts  = tableOfDatabases(page);
        String nav[] = new String[] {
            Replace.bracketQuote("Available Databases on <a href=[/]>server</a> ") + server
        };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    static String tableOfDatabases(DatabasesPage page) {
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
        return table(out.toString());
    }

    static String tablesOn(DatabasesPage page, Database database) {
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

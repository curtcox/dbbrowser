package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.html.CSS;
import com.cve.util.Replace;

import static com.cve.html.HTML.*;

/**
 * Renders a DatabasePage to a HTML string.
 */
public final class DatabasesPageRenderer implements ModelHtmlRenderer {

    public String render(Model model, ClientInfo client) {
        DatabasesPage page = (DatabasesPage) model;
        return render(page);
    }

    private String render(DatabasesPage page) {
        Server server = page.server;
        return 
            h1(Replace.bracketQuote("Available Databases on <a href=[/]>server</a> ") + server) +
            tableOfDatabases(page)
        ;
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

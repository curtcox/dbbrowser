package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.html.CSS;
import com.cve.util.Replace;

import com.cve.util.URIs;
import java.net.URI;
import static com.cve.html.HTML.*;
import static com.cve.web.db.NavigationButtons.*;
/**
 * For picking a table.
 */
public final class TablesPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Tables.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        TablesPage page = (TablesPage) model;
        Server     server = page.server;
        Database database = page.database;
        String guts = tableOfTables(page);
        String title = "Tables on " + server.uri + "/" + database.name;
        String[] nav = new String[] {
            Replace.bracketQuote(
                "Available Tables on <a href=[/]>server</a> /" +
                server.linkTo() + "/" + database.name
            ), SEARCH
        };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    static String tableOfTables(TablesPage page) {
        StringBuilder out = new StringBuilder();
        out.append(th("Table") + th("Columns"));
        for (DBTable table : page.tables) {
            out.append(tr(
                td(table.linkTo().toString(),CSS.TABLE) +
                td(columnsFor(page,table),CSS.COLUMN)
            ));
        }
        return table(out.toString());
    }

    static String columnsFor(TablesPage page, DBTable table) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (DBColumn column : page.columns.get(table)) {
            out.append(column.linkTo() + " ");
            i++;
            if (i>20) {
                out.append("...");
                return out.toString();
            }
        }
        return out.toString();
    }

}

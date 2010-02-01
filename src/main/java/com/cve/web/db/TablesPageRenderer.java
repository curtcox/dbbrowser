package com.cve.web.db;

import com.cve.web.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Replace;

import com.cve.util.URIs;
import java.net.URI;

/**
 * For picking a table.
 */
public final class TablesPageRenderer implements ModelHtmlRenderer {

    private final Log log = Logs.of();

    private final HTMLTags tags = HTMLTags.of();

    private static URI HELP = URIs.of("/resource/help/Tables.html");

    private TablesPageRenderer() {}

    public static TablesPageRenderer of() {
        return new TablesPageRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        TablesPage page = (TablesPage) model;
        DBServer     server = page.server;
        Database database = page.database;
        String guts = tableOfTables(page);
        String title = "Tables on " + server.uri + "/" + database.name;
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {
            Replace.bracketQuote(
                "Available Tables on <a href=[/]>server</a> /" +
                server.linkTo() + "/" + database.linkTo()
            ), b.SEARCH
        };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfTables(TablesPage page) {
        log.args(page);
        StringBuilder out = new StringBuilder();
        out.append(th("Table") + th("Rows") + th("Columns"));
        for (DBTable table : page.tables) {
            out.append(tr(
                td(table.linkTo().toString(),CSS.TABLE) +
                td("" + page.rows.get(table),CSS.ROW_COUNT) +
                td(columnsFor(page,table),CSS.COLUMN)
            ));
        }
        return table(out.toString());
    }

    String columnsFor(TablesPage page, DBTable table) {
        log.args(page,table);
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

    String table(String s)       { return tags.table(s); }
    String tr(String s)          { return tags.tr(s); }
    String th(String s)          { return tags.th(s); }
    String th(String s, CSS css) { return tags.th(s, css); }
    String td(String s, CSS css) { return tags.td(s, css); }
}

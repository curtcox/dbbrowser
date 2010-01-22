package com.cve.web.db;

import com.cve.web.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.html.CSS;
import com.cve.log.Log;
import com.cve.util.Replace;

import com.cve.util.URIs;
import java.net.URI;
import static com.cve.html.HTML.*;
import static com.cve.web.db.NavigationButtons.*;
import static com.cve.util.Check.notNull;

/**
 * For picking a table.
 */
public final class TablesPageRenderer implements ModelHtmlRenderer {

    private final Log log;

    private static URI HELP = URIs.of("/resource/help/Tables.html");

    private TablesPageRenderer(Log log) {
        this.log = notNull(log);
    }

    public static TablesPageRenderer of(Log log) {
        return new TablesPageRenderer(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.notNullArgs(model,client);
        TablesPage page = (TablesPage) model;
        DBServer     server = page.server;
        Database database = page.database;
        String guts = tableOfTables(page);
        String title = "Tables on " + server.uri + "/" + database.name;
        String[] nav = new String[] {
            Replace.bracketQuote(
                "Available Tables on <a href=[/]>server</a> /" +
                server.linkTo() + "/" + database.linkTo()
            ), SEARCH
        };
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String tableOfTables(TablesPage page) {
        log.notNullArgs(page);
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
        log.notNullArgs(page,table);
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

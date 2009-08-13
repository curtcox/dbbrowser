package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.html.CSS;
import com.cve.util.Replace;

import static com.cve.html.HTML.*;

/**
 * For picking a table.
 */
public final class TablesPageRenderer implements ModelRenderer {

    public Object render(Model model, ClientInfo client) {
        TablesPage page = (TablesPage) model;
        return render(page,client);
    }

    public String render(TablesPage page, ClientInfo client) {
        Server     server = page.server;
        Database database = page.database;
        return 
            h1(Replace.bracketQuote(
                "Available Tables on <a href=[/]>server</a> /" +
                server.linkTo() + "/" + database.name
            )) +
            tableOfTables(page)
        ;
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

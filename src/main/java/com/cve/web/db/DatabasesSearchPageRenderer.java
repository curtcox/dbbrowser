package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.web.*;
import com.cve.html.CSS;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITableBuilder;
import static com.cve.ui.UIBuilder.*;
import com.cve.util.URIs;
import com.google.common.collect.Sets;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import static com.cve.web.db.NavigationButtons.*;
import static com.cve.log.Log.args;

/**
 * For finding stuff in a database server.
 */
public final class DatabasesSearchPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        DatabasesSearchPage page = (DatabasesSearchPage) model;
        String title = "Available Servers";
        String[] navigation = new String[] {
            ADD_SERVER, REMOVE_SERVER , SHUTDOWN, title, search(page.search)
        };
        String guts  = Helper.render(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

/**
 * For rendering the search results page.
 * This is surprisingly complicated.  There is probably a much better way.
 */
static final class Helper {

    final DatabasesSearchPage page;

    static final UIDetail EMPTY_CELL = UIDetail.of("");

    Helper(DatabasesSearchPage page) {
        this.page = page;
    }

    static String render(DatabasesSearchPage page) {
        args(page);
        return new Helper(page).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        UITableBuilder out = new UITableBuilder();
        out.add(UIRow.of(detail("Database"),detail("Table"),detail("Columns")));
        for (Database database : page.databases) {
            if (isLeaf(database)) {
                out.add(row(database));
            } else {
                for (DBTable table : page.tables.get(database)) {
                    if (isLeaf(table)) {
                       out.add(row(table));
                    } else {
                        out.add(columnsRow(page.columns.get(table)));
                    }
                }
            }
        }
        return out.build().toString();
    }

    /**
     * Return true if there are no databases (which implies no tables, etc...)
     * under this server in the search results.
     */
    boolean isLeaf(Database database) {
        return !page.tables.containsKey(database);
    }

    boolean isLeaf(DBTable table) {
        return !page.columns.containsKey(table);
    }

    int height(Database database) {
        if (isLeaf(database)) {
            return 1;
        }
        return page.tables.get(database).size();
    }

    final Set<Database> databasesOut = Sets.newHashSet();
    UIDetail cell(Database database) {
        if (databasesOut.contains(database)) {
            return EMPTY_CELL;
        }
        databasesOut.add(database);
        int height = height(database);
        return RenderingTools.cell(database,height);
    }

    static UIDetail cell(DBTable table) {
        return RenderingTools.cell(table);
    }

    static UIDetail cell(Collection<DBColumn> columns) {
        return RenderingTools.cell(columns);
    }
    
    UIRow row(Database database) {
        return UIRow.of(cell(database));
    }

    UIRow row(DBTable table) {
        Database database = table.database;
        UIDetail databaseCell = cell(database);
        if (databaseCell==EMPTY_CELL) {
            return UIRow.of(cell(table));
        }
        return UIRow.of(
            databaseCell, cell(table)
        );
    }

    UIRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        Database database = table.database;
        UIDetail databaseCell = cell(database);
        UIDetail tableCell = cell(table);
        if (tableCell==EMPTY_CELL) {
            return UIRow.of(cell(columns));
        }
        if (databaseCell==EMPTY_CELL) {
            return UIRow.of(tableCell,cell(columns));
        }
        return UIRow.of(
            databaseCell, tableCell, cell(columns)
        );
    }
}

}

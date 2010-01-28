package com.cve.web.db.databases;

import com.cve.log.Log;
import com.cve.web.db.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.web.*;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITableBuilder;
import com.cve.util.Replace;
import com.cve.util.URIs;
import com.google.common.collect.Sets;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import static com.cve.util.Check.notNull;

/**
 * For finding stuff in a database server.
 */
final class DatabasesSearchPageRenderer implements ModelHtmlRenderer {

    final Log log;

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    private DatabasesSearchPageRenderer(Log log) {
        this.log = notNull(log);
    }

    static DatabasesSearchPageRenderer of(Log log) {
        return new DatabasesSearchPageRenderer(log);
    }
    
    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        DatabasesSearchPage page = (DatabasesSearchPage) model;
        String target = page.search.target;
        DBServer server = page.server;
        String title = "Occurences of " + target + " on " + server.toString();
        NavigationButtons b = NavigationButtons.of(log);
        String nav[] = new String[] {
            Replace.bracketQuote("Occurences of " + target + " on <a href=[/]>server</a> ") + server,
            b.search(page.search)
        };
        String guts  = Helper.render(page,log);
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP,log);
    }

/**
 * For rendering the search results page.
 * This is surprisingly complicated.  There is probably a much better way.
 */
static final class Helper {

    final DatabasesSearchPage page;

    final RenderingTools tools;

    final Log log;

    final UIDetail EMPTY_CELL;

    Helper(DatabasesSearchPage page, Log log) {
        this.page = notNull(page);
        this.log = notNull(log);
        tools = RenderingTools.of(log);
        EMPTY_CELL = UIDetail.of("",log);
    }

    static String render(DatabasesSearchPage page, Log log) {
        log.args(page);
        return new Helper(page,log).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        UITableBuilder out = UITableBuilder.of(log);
        out.add(row(detail("Database"),detail("Table"),detail("Columns")));
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
        return tools.cell(database,height);
    }

    UIDetail detail(String s) {
        return UIDetail.of(s, log);
    }

    UIDetail cell(DBTable table) {
        return tools.cell(table);
    }

    UIDetail cell(Collection<DBColumn> columns) {
        return tools.cell(columns);
    }
    
    UIRow row(Database database) {
        return UIRow.of(log,cell(database));
    }

    UIRow row(UIDetail... details) {
        return UIRow.of(log,details);
    }

    UIRow row(DBTable table) {
        Database database = table.database;
        UIDetail databaseCell = cell(database);
        if (databaseCell==EMPTY_CELL) {
            return row(cell(table));
        }
        return row(databaseCell, cell(table));
    }

    UIRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        Database database = table.database;
        UIDetail databaseCell = cell(database);
        UIDetail tableCell = cell(table);
        if (tableCell==EMPTY_CELL) {
            return row(cell(columns));
        }
        if (databaseCell==EMPTY_CELL) {
            return row(tableCell,cell(columns));
        }
        return row(databaseCell, tableCell, cell(columns));
    }
}

}

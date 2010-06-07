package com.cve.web.db.servers;

import com.cve.lang.URIObject;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.Search;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.db.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.web.*;
import com.cve.model.db.DBServer;
import com.cve.ui.UIElement;

import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITableBuilder;
import com.cve.util.URIs;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;
import static com.cve.util.Check.notNull;

/**
 * For finding stuff in a database server.
 */
final class ServersSearchPageRenderer implements ModelHtmlRenderer {


    final Log log = Logs.of();

    private static URIObject HELP = URIs.of("/resource/help/Servers.html");

    private ServersSearchPageRenderer() {
        
    }

    public static ServersSearchPageRenderer of() {
        return new ServersSearchPageRenderer();
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        log.args(model,client);
        ServersSearchPage page = (ServersSearchPage) model;
        Search          search = page.search;
        String title = "Occurences of " + search.target;
        NavigationButtons b = NavigationButtons.of();
        String[] navigation = new String[] {
            b.ADD_SERVER, b.REMOVE_SERVER , b.SHUTDOWN, title, b.search(search)
        };
        String guts  = Helper.render(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

/**
 * For rendering the search results page.
 * This is surprisingly complicated.  There is probably a much better way.
 */
static final class Helper {

    final Log log = Logs.of();

    final RenderingTools tools;

    final ServersSearchPage page;

    final UITableDetail EMPTY_CELL;

    Helper(ServersSearchPage page) {
        this.page = notNull(page);
        
        tools = RenderingTools.of();
        EMPTY_CELL = UITableDetail.of("");
    }

    static String render(ServersSearchPage page) {
        Logs.of().args(page);
        return new Helper(page).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        UITableBuilder out = UITableBuilder.of();
        out.add(row(detail("Database Server"),detail("Database"),detail("Table"),detail("Columns")));
        for (DBServer server : page.servers) {
            if (isLeaf(server)) {
                out.add(row(server));
            } else {
                for (Database database : page.databases.get(server)) {
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
            }
        }
        return out.build().toString();
    }

    /**
     * Return true if there are no databases (which implies no tables, etc...)
     * under this server in the search results.
     */
    boolean isLeaf(DBServer server) {
        return !page.databases.containsKey(server);
    }

    boolean isLeaf(Database database) {
        return !page.tables.containsKey(database);
    }

    boolean isLeaf(DBTable table) {
        return !page.columns.containsKey(table);
    }

    int height(DBServer server) {
        if (isLeaf(server)) {
            return 1;
        }
        int height = 0;
        for (Database database : page.databases.get(server)) {
            height += height(database);
        }
        return height;
    }

    int height(Database database) {
        if (isLeaf(database)) {
            return 1;
        }
        return page.tables.get(database).size();
    }

    final Set<DBServer> serversOut = Sets.newHashSet();
    UITableDetail cell(DBServer server) {
        if (serversOut.contains(server)) {
            return EMPTY_CELL;
        }
        serversOut.add(server);
        int height = height(server);
        return tools.cell(server,height);
    }

    final Set<Database> databasesOut = Sets.newHashSet();
    UITableDetail cell(Database database) {
        if (databasesOut.contains(database)) {
            return EMPTY_CELL;
        }
        databasesOut.add(database);
        int height = height(database);
        return tools.cell(database,height);
    }

    UITableRow row(DBServer server) {
        return row(cell(server));
    }

    UITableRow row(Database database) {
        UITableDetail serverCell = cell(database.server);
        if (serverCell==EMPTY_CELL) {
            return row(cell(database));
        }
        return row(serverCell,cell(database));
    }

    UITableRow row(DBTable table) {
        Database database = table.database;
        DBServer server     = database.server;
        UITableDetail serverCell = cell(server);
        UITableDetail databaseCell = cell(database);
        if (databaseCell==EMPTY_CELL) {
            return row(tools.cell(table));
        }
        if (serverCell==EMPTY_CELL) {
            return row(databaseCell,cell(table));
        }
        return row(serverCell, databaseCell, cell(table));
    }

    UITableRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        Database database = table.database;
        DBServer server     = database.server;
        UITableDetail serverCell = cell(server);
        UITableDetail databaseCell = cell(database);
        UITableDetail tableCell = cell(table);
        if (tableCell==EMPTY_CELL) {
            return row(cell(columns));
        }
        if (databaseCell==EMPTY_CELL) {
            return row(tableCell,cell(columns));
        }
        if (serverCell==EMPTY_CELL) {
            return row(databaseCell,tableCell,cell(columns));
        }
        return row( serverCell, databaseCell, tableCell, cell(columns) );
    }

    UITableRow row(UITableDetail... details) {
        return UITableRow.of( details);
    }

    UITableDetail cell(DBTable table) {
        return tools.cell(table);
    }

    UITableDetail detail(String s) {
        return UITableDetail.of(s);
    }

    UITableDetail cell(Collection<DBColumn> columns) {
        return tools.cell(columns);
    }
}

}

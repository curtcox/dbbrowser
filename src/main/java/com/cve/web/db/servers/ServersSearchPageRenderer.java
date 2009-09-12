package com.cve.web.db.servers;

import com.cve.web.db.*;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.web.*;
import com.cve.db.Server;

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
final class ServersSearchPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        ServersSearchPage page = (ServersSearchPage) model;
        Search          search = page.search;
        String title = "Occurences of " + search.target;
        String[] navigation = new String[] {
            ADD_SERVER, REMOVE_SERVER , SHUTDOWN, title, search(search)
        };
        String guts  = Helper.render(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP);
    }

/**
 * For rendering the search results page.
 * This is surprisingly complicated.  There is probably a much better way.
 */
static final class Helper {

    final ServersSearchPage page;

    static final UIDetail EMPTY_CELL = UIDetail.of("");

    Helper(ServersSearchPage page) {
        this.page = page;
    }

    static String render(ServersSearchPage page) {
        args(page);
        return new Helper(page).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        UITableBuilder out = new UITableBuilder();
        out.add(UIRow.of(detail("Database Server"),detail("Database"),detail("Table"),detail("Columns")));
        for (Server server : page.servers) {
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
    boolean isLeaf(Server server) {
        return !page.databases.containsKey(server);
    }

    boolean isLeaf(Database database) {
        return !page.tables.containsKey(database);
    }

    boolean isLeaf(DBTable table) {
        return !page.columns.containsKey(table);
    }

    int height(Server server) {
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

    final Set<Server> serversOut = Sets.newHashSet();
    UIDetail cell(Server server) {
        if (serversOut.contains(server)) {
            return EMPTY_CELL;
        }
        serversOut.add(server);
        int height = height(server);
        return RenderingTools.cell(server,height);
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

    UIRow row(Server server) {
        return UIRow.of(cell(server));
    }

    UIRow row(Database database) {
        UIDetail serverCell = cell(database.server);
        if (serverCell==EMPTY_CELL) {
            return UIRow.of(cell(database));
        }
        return UIRow.of(serverCell,cell(database));
    }

    UIRow row(DBTable table) {
        Database database = table.database;
        Server server     = database.server;
        UIDetail serverCell = cell(server);
        UIDetail databaseCell = cell(database);
        if (databaseCell==EMPTY_CELL) {
            return UIRow.of(RenderingTools.cell(table));
        }
        if (serverCell==EMPTY_CELL) {
            return UIRow.of(databaseCell,cell(table));
        }
        return UIRow.of(
            serverCell, databaseCell, cell(table)
        );
    }

    UIRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        Database database = table.database;
        Server server     = database.server;
        UIDetail serverCell = cell(server);
        UIDetail databaseCell = cell(database);
        UIDetail tableCell = cell(table);
        if (tableCell==EMPTY_CELL) {
            return UIRow.of(cell(columns));
        }
        if (databaseCell==EMPTY_CELL) {
            return UIRow.of(tableCell,cell(columns));
        }
        if (serverCell==EMPTY_CELL) {
            return UIRow.of(databaseCell,tableCell,cell(columns));
        }
        return UIRow.of(
            serverCell, databaseCell, tableCell, cell(columns)
        );
    }

    static UIDetail cell(DBTable table) {
        return RenderingTools.cell(table);
    }

    static UIDetail cell(Collection<DBColumn> columns) {
        return RenderingTools.cell(columns);
    }
}

}

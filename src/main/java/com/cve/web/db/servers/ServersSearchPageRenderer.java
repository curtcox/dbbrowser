package com.cve.web.db.servers;

import com.cve.log.Log;
import com.cve.web.db.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.web.*;
import com.cve.model.db.DBServer;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITableBuilder;
import com.cve.util.URIs;
import com.google.common.collect.Sets;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import static com.cve.util.Check.notNull;

/**
 * For finding stuff in a database server.
 */
final class ServersSearchPageRenderer implements ModelHtmlRenderer {


    final Log log;

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    private ServersSearchPageRenderer(Log log) {
        this.log = notNull(log);
    }

    public static ServersSearchPageRenderer of(Log log) {
        return new ServersSearchPageRenderer(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.notNullArgs(model,client);
        ServersSearchPage page = (ServersSearchPage) model;
        Search          search = page.search;
        String title = "Occurences of " + search.target;
        NavigationButtons b = NavigationButtons.of(log);
        String[] navigation = new String[] {
            b.ADD_SERVER, b.REMOVE_SERVER , b.SHUTDOWN, title, b.search(search)
        };
        String guts  = Helper.render(page,log);
        return HtmlPage.gutsTitleNavHelp(guts,title,navigation,HELP,log);
    }

/**
 * For rendering the search results page.
 * This is surprisingly complicated.  There is probably a much better way.
 */
static final class Helper {

    final Log log;

    final RenderingTools tools;

    final ServersSearchPage page;

    final UIDetail EMPTY_CELL;

    Helper(ServersSearchPage page, Log log) {
        this.page = notNull(page);
        this.log = notNull(log);
        tools = RenderingTools.of(log);
        EMPTY_CELL = UIDetail.of("",log);
    }

    static String render(ServersSearchPage page, Log log) {
        log.notNullArgs(page);
        return new Helper(page,log).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        UITableBuilder out = UITableBuilder.of(log);
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
    UIDetail cell(DBServer server) {
        if (serversOut.contains(server)) {
            return EMPTY_CELL;
        }
        serversOut.add(server);
        int height = height(server);
        return tools.cell(server,height);
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

    UIRow row(DBServer server) {
        return row(cell(server));
    }

    UIRow row(Database database) {
        UIDetail serverCell = cell(database.server);
        if (serverCell==EMPTY_CELL) {
            return row(cell(database));
        }
        return row(serverCell,cell(database));
    }

    UIRow row(DBTable table) {
        Database database = table.database;
        DBServer server     = database.server;
        UIDetail serverCell = cell(server);
        UIDetail databaseCell = cell(database);
        if (databaseCell==EMPTY_CELL) {
            return row(tools.cell(table));
        }
        if (serverCell==EMPTY_CELL) {
            return row(databaseCell,cell(table));
        }
        return row(serverCell, databaseCell, cell(table));
    }

    UIRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        Database database = table.database;
        DBServer server     = database.server;
        UIDetail serverCell = cell(server);
        UIDetail databaseCell = cell(database);
        UIDetail tableCell = cell(table);
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

    UIRow row(UIDetail... details) {
        return UIRow.of(log, details);
    }

    UIDetail cell(DBTable table) {
        return tools.cell(table);
    }

    UIDetail detail(String s) {
        return UIDetail.of(s, log);
    }

    UIDetail cell(Collection<DBColumn> columns) {
        return tools.cell(columns);
    }
}

}

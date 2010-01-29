package com.cve.web.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.web.*;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITableBuilder;
import com.cve.util.Replace;
import com.cve.util.URIs;
import java.net.URI;
import java.util.Collection;

/**
 * For finding stuff in a database server.
 */
public final class TablesSearchPageRenderer implements ModelHtmlRenderer {

    private final DBURICodec codec;

    private final Log log = Logs.of();

    private static URI HELP = URIs.of("/resource/help/TablesSearch.html");

    private TablesSearchPageRenderer() {
        
        codec = DBURICodec.of();
    }

    public static TablesSearchPageRenderer of() {
        return new TablesSearchPageRenderer();
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.args(model,client);
        TablesSearchPage page = (TablesSearchPage) model;
        Search     search = page.search;
        String     target = search.target;
        Database database = page.database;
        DBServer     server = database.server;
        String title = "Occurences of " + target + " on "+ server.uri + "/" + database.name;
        NavigationButtons b = NavigationButtons.of();
        String[] nav = new String[] {
            Replace.bracketQuote(
                "Occurences of " + target + " on <a href=[/]>server</a> /" +
                server.linkTo() + "/" + database.linkTo()
            ), b.search(page.search)
        };
        String guts  = Helper.of(page).render(page) + searchContentsLink(page);
        return HtmlPage.gutsTitleNavHelp(guts,title,nav,HELP);
    }

    String searchContentsLink(TablesSearchPage page) {
        Search     search = Search.contents(page.search.target);
        Database database = page.database;
        String    alt = "Search the table rows";
        Label    text = Label.of(alt);
        URI    target = codec.encode(search.ofContents(),database);
        URI     image = Icons.PLUS;
        return Link.textTargetImageAlt(text, target, image, alt).toString();
    }
/**
 * For rendering the search results page.
 * This is surprisingly complicated.  There is probably a much better way.
 */
static final class Helper {

    final TablesSearchPage page;

    final Log log = Logs.of();

    final UIDetail EMPTY_CELL;

    Helper(TablesSearchPage page) {
        this.page = page;
        EMPTY_CELL = UIDetail.of("");
    }

    static Helper of(TablesSearchPage page) {
        return new Helper(page);
    }

    String render(TablesSearchPage page) {
        log.args(page);
        return new Helper(page).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        Search search = page.search;
        if (page.columns.isEmpty()) {
            return search.target + " not found on " + page.database.linkTo();
        }
        UITableBuilder out = UITableBuilder.of();
        out.add(row(detail("Table"),detail("Columns")));
        for (DBTable table : page.tables) {
            if (isLeaf(table)) {
               out.add(row(table));
            } else {
                out.add(columnsRow(page.columns.get(table)));
            }
        }
        return out.build().toString();
    }

    boolean isLeaf(DBTable table) {
        return !page.columns.containsKey(table);
    }

    UIDetail cell(DBTable table) {
        return RenderingTools.of().cell(table);
    }

    UIDetail cell(Collection<DBColumn> columns) {
        return RenderingTools.of().cell(columns);
    }

    UIRow row(DBTable table) {
        return UIRow.of(cell(table));
    }

    UIRow row(UIDetail... details) {
        return UIRow.of(details);
    }

    UIDetail detail(String s) {
        return UIDetail.of(s);
    }

    UIRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        UIDetail tableCell = cell(table);
        if (tableCell==EMPTY_CELL) {
            return UIRow.of( cell(columns));
        }
        return UIRow.of(  tableCell, cell(columns) );
    }
}

}

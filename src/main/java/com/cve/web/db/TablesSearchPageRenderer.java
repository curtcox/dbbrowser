package com.cve.web.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.web.*;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITableBuilder;
import com.cve.util.Replace;
import static com.cve.ui.UIBuilder.*;
import com.cve.util.URIs;
import java.net.URI;
import java.util.Collection;
import static com.cve.web.db.NavigationButtons.*;
import static com.cve.util.Check.notNull;

/**
 * For finding stuff in a database server.
 */
public final class TablesSearchPageRenderer implements ModelHtmlRenderer {

    private final DBURICodec codec;

    private final Log log;

    private static URI HELP = URIs.of("/resource/help/TablesSearch.html");

    private TablesSearchPageRenderer(Log log) {
        this.log = notNull(log);
        codec = DBURICodec.of(log);
    }

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        log.notNullArgs(model,client);
        TablesSearchPage page = (TablesSearchPage) model;
        Search     search = page.search;
        String     target = search.target;
        Database database = page.database;
        DBServer     server = database.server;
        String title = "Occurences of " + target + " on "+ server.uri + "/" + database.name;
        String[] nav = new String[] {
            Replace.bracketQuote(
                "Occurences of " + target + " on <a href=[/]>server</a> /" +
                server.linkTo() + "/" + database.linkTo()
            ), search(page.search)
        };
        String guts  = Helper.of(page,log).render(page) + searchContentsLink(page);
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

    final Log log;

    static final UIDetail EMPTY_CELL = UIDetail.of("");

    Helper(TablesSearchPage page, Log log) {
        this.page = page;
        this.log = log;
    }

    static Helper of(TablesSearchPage page, Log log) {
        return new Helper(page,log);
    }

    String render(TablesSearchPage page) {
        log.notNullArgs(page);
        return new Helper(page,log).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
        Search search = page.search;
        if (page.columns.isEmpty()) {
            return search.target + " not found on " + page.database.linkTo();
        }
        UITableBuilder out = new UITableBuilder();
        out.add(UIRow.of(detail("Table"),detail("Columns")));
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

    static UIDetail cell(DBTable table) {
        return RenderingTools.cell(table);
    }

    static UIDetail cell(Collection<DBColumn> columns) {
        return RenderingTools.cell(columns);
    }

    UIRow row(DBTable table) {
        return UIRow.of(cell(table));
    }

    UIRow columnsRow(Collection<DBColumn> columns) {
        DBColumn   column = columns.iterator().next();
        DBTable     table = column.table;
        UIDetail tableCell = cell(table);
        if (tableCell==EMPTY_CELL) {
            return UIRow.of(cell(columns));
        }
        return UIRow.of( tableCell, cell(columns) );
    }
}

}

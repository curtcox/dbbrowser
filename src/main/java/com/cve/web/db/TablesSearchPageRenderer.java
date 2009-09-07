package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.web.*;
import com.cve.html.CSS;

import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITableBuilder;
import static com.cve.ui.UIBuilder.*;
import com.cve.util.URIs;
import java.net.URI;
import java.util.Collection;
import static com.cve.web.db.NavigationButtons.*;
import static com.cve.log.Log.args;

/**
 * For finding stuff in a database server.
 */
public final class TablesSearchPageRenderer implements ModelHtmlRenderer {

    private static URI HELP = URIs.of("/resource/help/Servers.html");

    @Override
    public HtmlPage render(Model model, ClientInfo client) {
        args(model,client);
        TablesSearchPage page = (TablesSearchPage) model;
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

    final TablesSearchPage page;

    static final UIDetail EMPTY_CELL = UIDetail.of("");

    Helper(TablesSearchPage page) {
        this.page = page;
    }

    static String render(TablesSearchPage page) {
        args(page);
        return new Helper(page).render();
    }
    
    /**
     * Return a table of all the available servers.
     */
    String render() {
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

package com.cve.web.db.render;

import com.cve.html.HTMLTags;
import com.cve.web.db.SelectBuilderAction;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.DBColumn;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBTable;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * Things hidden in the result set that could be shown.
 * If empty, then skip the table.  If not empty, it looks like:
 *
 * ------|
 * |     |table1 | column1, c2, c3
 * |show |table2 | c4, c5
 * |     |table3 | c6
 * ------|
 */
@Immutable

public final class ShowTableRenderer {

    private final Log log = Logs.of();

    private final HTMLTags tags;
    /**
     * The results we render
     */
    private final SelectResults results;

    private ShowTableRenderer(SelectResults results) {
        this.results = notNull(results);
        
        this.tags = HTMLTags.of();
    }

    static ShowTableRenderer results(SelectResults results) {
        return new ShowTableRenderer(results);
    }

    public static String render(SelectResults results) {
        ShowTableRenderer renderer = new ShowTableRenderer(results);
        return renderer.showTable();
   }

    /**
     * Return a table of currently hidden things that can be shown.
     * If empty, then skip the table.  If not empty, it looks like:
     *
     * ------|
     * |     |table1 | column1, c2, c3
     * |show |table2 | c4, c5
     * |     |table3 | c6
     * ------|
     */
    String showTable() {
        StringBuilder tableOut = new StringBuilder();
        ImmutableList<DBTable> tables = getTablesWithHiddenColumns();
        if (tables.isEmpty()) {
            return "";
        }
        int tableCount = 0;
        for (DBTable table : results.resultSet.tables) {
            ImmutableList<DBColumn> columns = getHiddenColumnsFor(table);
            StringBuilder rowOut = new StringBuilder();
            if (tableCount==0) {
                rowOut.append(tdRowspan("Show",tables.size()));
            }
            rowOut.append(td(table.fullName()));
            tableCount++;
            StringBuilder shows = new StringBuilder();
            for (DBColumn column : columns) {
                shows.append(showCell(column) + " ");
            }
            String row = rowOut.toString() + td(shows.toString());
            tableOut.append(tr(row));
        }
        return borderTable(tableOut.toString());
    }

    public String    tdRowspan(String s, int height) { return "<td rowspan=" + HTMLTags.of().q(height) + ">" + s + "</td>"; }

    ImmutableList<DBTable> getTablesWithHiddenColumns() {
        return ImmutableList.copyOf(Collections2.filter(results.resultSet.tables, new Predicate() {
            public boolean apply(Object o) {
                DBTable t = (DBTable) o;
                return !getHiddenColumnsFor(t).isEmpty();
            }
        }));
    }

    ImmutableList<DBColumn> getHiddenColumnsFor(DBTable table) {
        return ImmutableList.copyOf(Collections2.filter(getAllColumnsFor(table), new Predicate() {
            public boolean apply(Object o) {
                DBColumn c = (DBColumn) o;
                return !results.resultSet.columns.contains(c);
            }
        }));
    }

    /**
     * Return all of the columns for the given table.
     */
    ImmutableList<DBColumn> getAllColumnsFor(DBTable table) {
        List<DBColumn> list = Lists.newArrayList();
        for (DBColumn column : results.hints.columns) {
            if (column.table.equals(table)) {
                list.add(column);
            }
        }
        return ImmutableList.copyOf(list);
    }


    String showCell(DBColumn column) {
        Label  text = Label.of(column.name);
        URI  target = SelectBuilderAction.SHOW.withArgs(column.fullName());
        return Link.textTarget(text, target).toString();
    }

    String h1(String s) { return tags.h1(s); }
    String h2(String s) { return tags.h2(s); }
    String tr(String s) { return tags.tr(s); }
    String td(String s) { return tags.td(s); }
String th(String s) { return tags.th(s); }
String table(String s) { return tags.table(s); }
String borderTable(String s) { return tags.borderTable(s); }

}

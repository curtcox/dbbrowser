package com.cve.db.render;

import com.cve.web.db.SelectBuilderAction;
import com.cve.db.Cell;
import com.cve.html.Label;
import com.cve.html.Tooltip;
import com.cve.html.Link;
import com.cve.ui.UIDetail;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.Filter;
import com.cve.db.Hints;
import com.cve.db.Join;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.Value;
import com.cve.db.select.URIRenderer;
import com.cve.html.CSS;
import com.cve.web.ClientInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.concurrent.Immutable;
import static com.cve.html.HTML.*;
import static com.cve.util.Check.notNull;

/**
 * Tools for rendering result sets as HTML.
 * Renders the results of a database select as a single HTML table.
 */
@Immutable
public final class ResultsTableRenderingTools {

    /**
     * The results we render
     */
    private final SelectResults results;

    /**
     * Information about what we are rendering to.
     */
    private final ClientInfo client;

    public static final String HIDE = "x";

    private ResultsTableRenderingTools(SelectResults results, ClientInfo client) {
        this.results = notNull(results);
        this.client  = notNull(client);
    }

    static ResultsTableRenderingTools results(SelectResults results, ClientInfo client) {
        notNull(results);
        notNull(client);
        return new ResultsTableRenderingTools(results,client);
    }

    public static String    tdRowspan(String s, int width) { return "<td rowspan=" + q(width) + ">" + s + "</td>"; }

    /**
     * A table row where each cell represents a different database.
     * Cells from this row may span multiple columns of rows below.
     */
    ImmutableList<UIDetail> databaseRow() {
        DBResultSet resultSet = results.resultSet;
        List<UIDetail> out = Lists.newArrayList();
        for (Database database : resultSet.databases) {
            int width = results.resultSet.columns.size();
            out.add(UIDetail.of(nameCell(database),width));
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * A table row where each cell represents a different table.
     * Cells from this row may span multiple columns of rows below.
     */
    ImmutableList<UIDetail> tableRow() {
        DBResultSet resultSet = results.resultSet;
        List<UIDetail> out = Lists.newArrayList();
        for (DBTable table : resultSet.tables) {
            int width = 0;
            for (DBColumn c: results.resultSet.columns) {
                if (table.equals(c.table)) {
                    width++;
                }
            }
            out.add(UIDetail.of(nameCell(table),width));
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * A table row where each cell represents a different column.
     * Cells in this row map one-to-one to columns in the result set.
     */
    ImmutableList<UIDetail> columnNameRow() {
        DBResultSet resultSet = results.resultSet;
        List<UIDetail> out = Lists.newArrayList();
        int columnCount = resultSet.columns.size();
        for (DBColumn column : resultSet.columns) {
            if (columnCount < 5 ) {
                out.add(UIDetail.of(nameCell(column),classOf(column)));
            } else {
                out.add(UIDetail.of(nameCell(column),classOf(column)));
            }
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * A table row where each cell a link to hide the column.
     * Cells in this row map one-to-one to columns in the result set.
     */
    ImmutableList<UIDetail> columnHideRow() {
        DBResultSet resultSet = results.resultSet;
        List<UIDetail> out = Lists.newArrayList();
        for (DBColumn column : resultSet.columns) {
            out.add(UIDetail.of(hideCell(column)));
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * The rows that contain all of the result set values.
     */
    String valueRows() {
        DBResultSet resultSet = results.resultSet;
        StringBuilder out = new StringBuilder();
        CSS cssClass = CSS.ODD_ROW;
        for (DBRow row : resultSet.rows) {
            out.append("<tr class=\"" + cssClass + "\">");
            for (DBColumn column : resultSet.columns) {
                Cell cell = Cell.at(row, column);
                Value value = resultSet.getValue(row, column);
                out.append(td(valueCell(cell,value)));
            }
            out.append("</tr>\r");
            if (cssClass==CSS.EVEN_ROW) {
                cssClass = CSS.ODD_ROW;
            } else {
                cssClass = CSS.EVEN_ROW;
            }
        }
        return out.toString();
    }

    /**
     * Return a string to go in a database name cell.
     */
    static String nameCell(Database database) {
        Label  text = Label.of(database.name);
        URI  target = linkTo(database);
        return "Database : " + Link.textTarget(text,target).toString();
    }

    /**
     * Return a string to go in a table name cell.
     */
    static String nameCell(DBTable table) {
        Label  text = Label.of(table.name);
        URI  target = linkTo(table);
        return "Table : " + Link.textTarget(text,target).toString();
    }

    /**
     * Produce the contents of a column name cell.
     * The cell should link to a page for the column.
     * It should also have a tooltip with a description to the column and links
     * for relevant hints (joins and filters);
     */
    String nameCell(DBColumn column) {
        int                     width = maxWidth(column);
        String             columnName = breakUp(column.name);

        if (columnName.length() > width) {
            columnName = columnName.substring(0,width);
        }
        Label                    text = Label.of(columnName);
        URI                    target = linkTo(column);
        Hints                   hints = results.hints;
        ImmutableList<DBColumn>   joins = destinationColumns(column,hints.getJoinsFor(column));
        ImmutableList<Filter> filters = hints.getFiltersFor(column);
        Tooltip tooltip = ColumnNameTooltip.columnJoinsFilters(column,joins,filters);
        return Link.textTargetTip(text, target, tooltip).toString();
    }

    /**
     * Column names tend to be wider than any data in the column.
     * Often they are composed of separate words, like account_history_status.
     * By replacing underscores and hyphens with spaces, we give the browser
     * more freedom to render the table in less space.
     */
    static String breakUp(String columnName) {
        return columnName.replace("_"," ").replace("-", " ");
    }

    /**
     * Return the maximum width (in characters) of all cells in this column.
     */
    int maxWidth(DBColumn column) {
        return 30;
    }

    /**
     * Return the list of Columns you might want to join this column to.
     */
    ImmutableList<DBColumn> destinationColumns(DBColumn column, ImmutableList<Join> joins) {
        Set<DBColumn> set = Sets.newHashSet();
        for (Join join : joins) {
            set.add(join.source);
            set.add(join.dest);
        }
        set.remove(column);
        List<DBColumn> ordered = Lists.newArrayList(set);
        Collections.sort(ordered, new ColumnProximityComparator(column));
        return ImmutableList.copyOf(ordered);
    }

    CSS classOf(DBColumn column) {
        Hints                   hints = results.hints;
        ImmutableList<DBColumn>   joins = destinationColumns(column,hints.getJoinsFor(column));
        boolean              hasJoins = joins.size() > 0;
        if (hasJoins) {
            return CSS.COLUMN_JOIN;
        }
        return CSS.COLUMN;
    }

    static String hideCell(DBColumn column) {
        Label  text = Label.of(HIDE);
        URI  target = SelectBuilderAction.HIDE.withArgs(column.fullName());
        return Link.textTarget(text, target).toString();
    }

    static String valueCell(Cell cell, Value value) {
        Object       object = value.getValue();
        String  valueString = "" + object;
        Label          text = Label.of(valueString);
        DBColumn       column = cell.column;
        String encodedValue = URLEncoder.encode(valueString);
        URI          target = SelectBuilderAction.FILTER.withArgs(column.fullName() + "=" + encodedValue);
        return Link.textTarget(text, target).toString();
    }

    static URI linkTo(DBColumn column) {
        DBTable       table = column.table;
        Database database = table.database;
        Server     server = database.server;

        return URIRenderer.render(server,database,table,column);
    }

    static URI linkTo(DBTable table) {
        Database database = table.database;
        Server     server = database.server;
        return URIRenderer.render(server,database,table);
    }

    static URI linkTo(Database database) {
        Server     server = database.server;
        return URIRenderer.render(server,database);
    }

}

package com.cve.web.db.render;

import com.cve.web.db.SelectBuilderAction;
import com.cve.db.Cell;
import com.cve.html.Label;
import com.cve.html.Tooltip;
import com.cve.html.Link;
import com.cve.ui.UIDetail;
import com.cve.db.DBColumn;
import com.cve.db.DBColumn.Keyness;
import com.cve.db.Database;
import com.cve.db.Filter;
import com.cve.db.Hints;
import com.cve.db.Join;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.DBTable;
import com.cve.db.Order;
import com.cve.db.Value;
import com.cve.html.CSS;
import com.cve.html.HTML;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.ClientInfo;
import com.cve.web.Icons;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.concurrent.Immutable;
import static com.cve.html.HTML.*;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * Tools for rendering result sets as HTML.
 * Renders the results of a database select as a single HTML table.
 */
@Immutable
public final class DBResultSetRenderer {

    /**
     * Hints about how we might want to join, filter, or otherwise manipulate this.
     */
    private final Hints hints;

    /**
     * The results we render
     */
    private final DBResultSet results;

    /**
     * How the given results are ordered.
     */
    private final ImmutableList<Order> orders;

    /**
     * Information about what we are rendering to.
     */
    private final ClientInfo client;

    private DBResultSetRenderer(DBResultSet results, ImmutableList<Order> orders, Hints hints, ClientInfo client) {
        this.results = notNull(results);
        this.orders  = notNull(orders);
        this.hints   = notNull(hints);
        this.client  = notNull(client);
    }

    public static DBResultSetRenderer resultsOrdersHintsClient(DBResultSet results, ImmutableList<Order> orders, Hints hints, ClientInfo client) {
        notNull(results);
        notNull(client);
        return new DBResultSetRenderer(results,orders,hints,client);
    }

    public static String    tdRowspan(String s, int width) { return "<td rowspan=" + q(width) + ">" + s + "</td>"; }

    /**
     * Return a landscape table where every result set row maps to a table row.
     */
    public String landscapeTable() {
        List<UIRow> rows = Lists.newArrayList();
        rows.add(UIRow.of(databaseRow(),   CSS.DATABASE));
        rows.add(UIRow.of(tableRow(),      CSS.TABLE));
        rows.add(UIRow.of(columnNameRow()));
        rows.add(UIRow.of(columnActionsRow(), CSS.ACTIONS));
        rows.addAll(valueRowsList());
        return UITable.of(rows).toString();
    }

    /**
     * The rows that contain all of the result set values.
     */
    List<UIRow> valueRowsList() {
        List<UIRow> out = Lists.newArrayList();
        CSS cssClass = CSS.ODD_ROW;
        for (DBRow row : results.rows) {
            List<UIDetail> details = Lists.newArrayList();
            for (DBColumn column : results.columns) {
                Cell cell = Cell.at(row, column);
                Value value = results.getValue(row, column);
                details.add(UIDetail.of(valueCell(cell,value)));
            }
            out.add(UIRow.of(details, cssClass));
            if (cssClass==CSS.EVEN_ROW) {
                cssClass = CSS.ODD_ROW;
            } else {
                cssClass = CSS.EVEN_ROW;
            }
        }
        return out;
    }

    /**
     * A table row where each cell represents a different database.
     * Cells from this row may span multiple columns of rows below.
     */
    ImmutableList<UIDetail> databaseRow() {
        List<UIDetail> out = Lists.newArrayList();
        for (Database database : results.databases) {
            int width = results.columns.size();
            out.add(UIDetail.of(nameCell(database),width));
        }
        return ImmutableList.copyOf(out);
    }

    /**
     * A table row where each cell represents a different table.
     * Cells from this row may span multiple columns of rows below.
     */
    ImmutableList<UIDetail> tableRow() {
        List<UIDetail> out = Lists.newArrayList();
        for (DBTable table : results.tables) {
            int width = 0;
            for (DBColumn c: results.columns) {
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
        List<UIDetail> out = Lists.newArrayList();
        int columnCount = results.columns.size();
        for (DBColumn column : results.columns) {
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
    ImmutableList<UIDetail> columnActionsRow() {
        List<UIDetail> out = Lists.newArrayList();
        for (DBColumn column : results.columns) {
            out.add(UIDetail.of(actionCell(column,direction(column))));
        }
        return ImmutableList.copyOf(out);
    }

    Order.Direction direction(DBColumn column) {
        for (Order order : orders) {
            if (order.column.equals(column)) {
                return order.direction;
            }
        }
        return Order.Direction.NONE;
    }

    /**
     * The rows that contain all of the result set values.
     */
    String valueRows() {
        StringBuilder out = new StringBuilder();
        CSS cssClass = CSS.ODD_ROW;
        for (DBRow row : results.rows) {
            out.append("<tr class=\"" + cssClass + "\">");
            for (DBColumn column : results.columns) {
                Cell cell = Cell.at(row, column);
                Value value = results.getValue(row, column);
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
        args(database);
        Label  text = Label.of(database.name);
        URI  target = database.linkTo().getTarget();
        return "Database : " + Link.textTarget(text,target).toString();
    }

    /**
     * Return a string to go in a table name cell.
     */
    static String nameCell(DBTable table) {
        args(table);
        Label  text = Label.of(table.name);
        URI  target = table.linkTo().getTarget();
        return "Table : " + Link.textTarget(text,target).toString();
    }

    /**
     * Produce the contents of a column name cell.
     * The cell should link to a page for the column.
     * It should also have a tooltip with a description to the column and links
     * for relevant hints (joins and filters);
     */
    String nameCell(DBColumn column) {
        args(column);
        int                     width = maxWidth(column);
        String             columnName = breakUp(column.name);

        if (columnName.length() > width) {
            columnName = columnName.substring(0,width);
        }
        Label                    text = Label.of(columnName);
        URI                    target = column.linkTo().getTarget();
        ImmutableList<DBColumn> joins = destinationColumns(column,hints.getJoinsFor(column));
        ImmutableList<Filter> filters = hints.getFiltersFor(column);
        Tooltip tooltip = ColumnNameTooltip.columnJoinsFilters(column,joins,filters);
        String link = Link.textTargetTip(text, target, tooltip).toString();
        Keyness keyness = column.keyness;
        if (keyness==DBColumn.Keyness.NONE) {
            return link;
        }
        if (keyness==DBColumn.Keyness.PRIMARY) {
            return HTML.img("Primary key", Icons.PRIMARY_KEY) + link;
        }
        if (keyness==DBColumn.Keyness.FOREIGN) {
            return HTML.img("Foreign key", Icons.FOREIGN_KEY) + link;
        }
        throw new IllegalArgumentException("" + keyness);
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
        ImmutableList<DBColumn>   joins = destinationColumns(column,hints.getJoinsFor(column));
        boolean              hasJoins = joins.size() > 0;
        if (hasJoins) {
            return CSS.COLUMN_JOIN;
        }
        return CSS.COLUMN;
    }

    static String actionCell(DBColumn column, Order.Direction direction) {
        Label  text = Label.of("Hide or sort");
        URI  target = SelectBuilderAction.HIDE.withArgs(column.fullName());
        Tooltip tip = ColumnActionTooltip.columnDirection(column,direction);
        URI   image = Icons.CONFIGURE;
        return Link.textTargetTipImage(text, target, tip,image).toString();
    }

    static String valueCell(Cell cell, Value value) {
        Object       object = value.value;
        String  valueString = "" + object;
        Label          text = Label.of(valueString);
        DBColumn     column = cell.column;
        Filter       filter = Filter.of(column, value);
        URI          target = SelectBuilderAction.FILTER.withArgs(filter.toUrlFragment());
        return Link.textTarget(text, target).toString();
    }

}
package com.cve.web.db.render;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.Cell;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBValue;
import com.cve.html.CSS;
import com.cve.ui.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UITableDetail;
import com.cve.ui.UITableRow;
import com.cve.ui.UITable;
import com.cve.ui.UITableCell;
import com.cve.web.core.ClientInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
import static com.cve.web.db.render.DBResultSetRenderer.*;

/**
 * Renders the results of a database select as a single HTML table.
 */
@Immutable

public final class DistributionResultsTableRenderer {

    /**
     * The results we render
     */
    private final SelectResults results;

    /**
     * Information about what we are rendering to.
     */
    private final ClientInfo client;

    final Log log = Logs.of();
    
    /**
     * Utility methods for rendering select results.
     */
    private final DBResultSetRenderer tools;

    private DistributionResultsTableRenderer(SelectResults results, ClientInfo client) {
        this.results = notNull(results);
        this.client  = notNull(client);
        tools = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet, results.select.orders, results.hints, client);
    }

    static DistributionResultsTableRenderer results(SelectResults results, ClientInfo client) {
        return new DistributionResultsTableRenderer(results,client);
    }

    static String render(SelectResults results, ClientInfo client) {
        return new DistributionResultsTableRenderer(results,client).resultsTable();
    }

    public String    tdRowspan(String s, int width) { return "<td rowspan=" + HTMLTags.of().q(width) + ">" + s + "</td>"; }


    /**
     * Return a landscape table where every result set row maps to a table row.
     */
    String resultsTable() {

        List<UITableRow> rows = Lists.newArrayList();
        rows.add(row(tools.databaseRow(),   CSS.DATABASE));
        rows.add(row(tools.tableRow(),      CSS.TABLE));
        rows.add(row(columnNameRow()));
        rows.addAll(valueRows());
        return UITable.of(rows).toString();
    }

    UITableRow row(List<UITableCell> details, CSS css) { return UITableRow.of(details,css); }
    UITableRow row(List<UITableCell> details)          { return UITableRow.of(details); }
    UITableDetail detail(String value, CSS css)     { return UITableDetail.of(value,css); }
    UITableDetail detail(String value)              { return UITableDetail.of(value); }

     /**
     * A table row where each cell represents a different column.
     * Cells in this row map one-to-one to columns in the result set.
     */
    ImmutableList<UITableCell> columnNameRow() {
        DBResultSet resultSet = results.resultSet;
        List<UITableCell> out = Lists.newArrayList();
        DBColumn column = resultSet.columns.get(0);
        out.add(detail(column.name,tools.classOf(column)));
        out.add(detail("count"));
        return ImmutableList.copyOf(out);
    }

    /**
     * The rows that contain all of the result set values.
     */
    List<UITableRow> valueRows() {
        DBResultSet resultSet = results.resultSet;
        List<UITableRow> out = Lists.newArrayList();
        CSS cssClass = CSS.ODD_ROW;
        for (DBRow row : resultSet.rows) {
            List<UITableCell> details = Lists.newArrayList();
            DBColumn column = resultSet.columns.get(0);

            Cell   valueCell = Cell.at(row, column);
            DBValue      value = resultSet.getValue(row, column);
            details.add(detail(tools.valueCell(valueCell,value)));

            Cell    countCell = Cell.at(row, column,AggregateFunction.COUNT);
            DBValue  countValue = resultSet.values.get(countCell);
            details.add(detail(countValue.value.toString()));

            out.add(row(details, cssClass));
            if (cssClass==CSS.EVEN_ROW) {
                cssClass = CSS.ODD_ROW;
            } else {
                cssClass = CSS.EVEN_ROW;
            }
        }
        return out;
    }

}

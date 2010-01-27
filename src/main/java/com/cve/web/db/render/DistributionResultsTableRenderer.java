package com.cve.web.db.render;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.Cell;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBValue;
import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.ClientInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.html.HTML.*;
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

    final Log log;
    
    /**
     * Utility methods for rendering select results.
     */
    private final DBResultSetRenderer tools;

    private DistributionResultsTableRenderer(SelectResults results, ClientInfo client, Log log) {
        this.results = notNull(results);
        this.client  = notNull(client);
        this.log     = notNull(log);
        tools = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet, results.select.orders, results.hints, client,log);
    }

    static DistributionResultsTableRenderer results(SelectResults results, ClientInfo client, Log log) {
        return new DistributionResultsTableRenderer(results,client,log);
    }

    static String render(SelectResults results, ClientInfo client,Log log) {
        return new DistributionResultsTableRenderer(results,client,log).resultsTable();
    }

    public String    tdRowspan(String s, int width) { return "<td rowspan=" + HTMLTags.of(log).q(width) + ">" + s + "</td>"; }


    /**
     * Return a landscape table where every result set row maps to a table row.
     */
    String resultsTable() {

        List<UIRow> rows = Lists.newArrayList();
        rows.add(row(tools.databaseRow(),   CSS.DATABASE));
        rows.add(row(tools.tableRow(),      CSS.TABLE));
        rows.add(row(columnNameRow()));
        rows.addAll(valueRows());
        return UITable.of(rows,log).toString();
    }

    UIRow row(List<UIDetail> details, CSS css) { return UIRow.of(details,css,log); }
    UIRow row(List<UIDetail> details)          { return UIRow.of(details,log); }
    UIDetail detail(String value, CSS css)     { return UIDetail.of(value,css,log); }
    UIDetail detail(String value)              { return UIDetail.of(value,log); }

     /**
     * A table row where each cell represents a different column.
     * Cells in this row map one-to-one to columns in the result set.
     */
    ImmutableList<UIDetail> columnNameRow() {
        DBResultSet resultSet = results.resultSet;
        List<UIDetail> out = Lists.newArrayList();
        DBColumn column = resultSet.columns.get(0);
        out.add(detail(column.name,tools.classOf(column)));
        out.add(detail("count"));
        return ImmutableList.copyOf(out);
    }

    /**
     * The rows that contain all of the result set values.
     */
    List<UIRow> valueRows() {
        DBResultSet resultSet = results.resultSet;
        List<UIRow> out = Lists.newArrayList();
        CSS cssClass = CSS.ODD_ROW;
        for (DBRow row : resultSet.rows) {
            List<UIDetail> details = Lists.newArrayList();
            DBColumn column = resultSet.columns.get(0);

            Cell   valueCell = Cell.at(row, column);
            DBValue      value = resultSet.getValue(row, column);
            details.add(detail(valueCell(valueCell,value)));

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

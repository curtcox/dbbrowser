package com.cve.db.render;

import com.cve.db.AggregateFunction;
import com.cve.db.Cell;
import com.cve.db.DBColumn;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.SelectResults;
import com.cve.db.Value;
import com.cve.html.CSS;
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
import static com.cve.db.render.ResultsTableRenderingTools.*;

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

    /**
     * Utility methods for rendering select results.
     */
    private final ResultsTableRenderingTools tools;

    private DistributionResultsTableRenderer(SelectResults results, ClientInfo client) {
        this.results = notNull(results);
        this.client  = notNull(client);
        tools = ResultsTableRenderingTools.results(results, client);
    }

    static DistributionResultsTableRenderer results(SelectResults results, ClientInfo client) {
        return new DistributionResultsTableRenderer(results,client);
    }

    static String render(SelectResults results, ClientInfo client) {
        return new DistributionResultsTableRenderer(results,client).resultsTable();
    }

    public static String    tdRowspan(String s, int width) { return "<td rowspan=" + q(width) + ">" + s + "</td>"; }


    /**
     * Return a landscape table where every result set row maps to a table row.
     */
    String resultsTable() {

        List<UIRow> rows = Lists.newArrayList();
        rows.add(UIRow.of(tools.databaseRow(),   CSS.DATABASE));
        rows.add(UIRow.of(tools.tableRow(),      CSS.TABLE));
        rows.add(UIRow.of(columnNameRow()));
        rows.addAll(valueRows());
        return UITable.of(rows).toString();
    }

     /**
     * A table row where each cell represents a different column.
     * Cells in this row map one-to-one to columns in the result set.
     */
    ImmutableList<UIDetail> columnNameRow() {
        DBResultSet resultSet = results.resultSet;
        List<UIDetail> out = Lists.newArrayList();
        DBColumn column = resultSet.columns.get(0);
        out.add(UIDetail.of(column.name,tools.classOf(column)));
        out.add(UIDetail.of("count"));
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
            Value      value = resultSet.getValue(row, column);
            details.add(UIDetail.of(valueCell(valueCell,value)));

            Cell    countCell = Cell.at(row, column,AggregateFunction.COUNT);
            Value  countValue = resultSet.values.get(countCell);
            details.add(UIDetail.of(countValue.getValue().toString()));

            out.add(UIRow.of(details, cssClass));
            if (cssClass==CSS.EVEN_ROW) {
                cssClass = CSS.ODD_ROW;
            } else {
                cssClass = CSS.EVEN_ROW;
            }
        }
        return out;
    }

}

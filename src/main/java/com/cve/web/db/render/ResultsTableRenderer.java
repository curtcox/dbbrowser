package com.cve.web.db.render;

import com.cve.model.db.Cell;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.DBRow;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBTable;
import com.cve.model.db.Order;
import com.cve.model.db.DBValue;
import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.ClientInfo;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
import static com.cve.web.db.render.DBResultSetRenderer.*;

/**
 * Renders the results of a database select as a single HTML table.
 */
@Immutable

public final class ResultsTableRenderer {

    private final Log log = Logs.of();
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
    private final DBResultSetRenderer tools;

    private ResultsTableRenderer(SelectResults results, ClientInfo client) {
        this.results = notNull(results);
        this.client  = notNull(client);
        
        tools = DBResultSetRenderer.resultsOrdersHintsClient(results.resultSet, results.select.orders, results.hints, client);
    }

    static ResultsTableRenderer resultsClientInfo(SelectResults results, ClientInfo client) {
        return new ResultsTableRenderer(results,client);
    }

    public static String render(SelectResults results, ClientInfo client) {
        return new ResultsTableRenderer(results,client).resultsTable();
    }

    String tdRowspan(String s, int width) { return "<td rowspan=" + HTMLTags.of().q(width) + ">" + s + "</td>"; }


    String resultsTable() {
        // Tables with few enough rows get printed in portrait
        if (results.resultSet.rows.size() < 4) {
            return portraitTable();
        }
        return tools.landscapeTable();
    }

    /**
     * Return a portrait table where every result set row maps to a table column.
     */
    String portraitTable() {
        List<UIRow> out = Lists.newArrayList();
        DBResultSet resultSet = results.resultSet;
        UIRow headerRow = row(
            detail("Database",CSS.DATABASE),
            detail("Table",CSS.TABLE),
            detail("Column",CSS.COLUMN),
            detail("Action",CSS.ACTIONS),
            detail("Value"));
        out.add(headerRow);
        Database lastDatabase = Database.NULL;
        DBTable     lastTable = DBTable.NULL;
        for (DBColumn column : resultSet.columns) {
            List<UIDetail> details = Lists.newArrayList();
            DBTable          table = column.table;
            Database      database = table.database;
            if (database.equals(lastDatabase)) {
                details.add(detail("",CSS.DATABASE));
            } else {
                details.add(detail(nameCell(database),CSS.DATABASE));
                lastDatabase = database;
            }
            if (table.equals(lastTable)) {
                details.add(detail("",CSS.TABLE));
            } else {
                details.add(detail(nameCell(table),CSS.TABLE));
                lastTable = table;
            }
            details.add(detail(nameCell(column),tools.classOf(column)));
            details.add(detail(actionCell(column,direction(column)),CSS.ACTIONS));
            for (DBRow row : resultSet.rows) {
                Cell cell = Cell.at(row, column);
                DBValue value = resultSet.getValue(row, column);
                details.add(detail(valueCell(cell,value)));
            }
            out.add(row(details));
        }
        out.add(headerRow);
        return UITable.of(out).toString();
    }

    UIRow       row(List<UIDetail> details) { return UIRow.of(details);       }
    UIRow       row(UIDetail... details)    { return UIRow.of( details);       }
    UIDetail detail(String value , CSS css) { return UIDetail.of(value, css); }
    UIDetail detail(String value)           { return UIDetail.of(value); }
    String nameCell(DBColumn column)        { return tools.nameCell(column);   }
    String nameCell(Database database)      { return tools.nameCell(database); }
    String nameCell(DBTable table)          { return tools.nameCell(table);  }
    String actionCell(DBColumn column, Order.Direction direction) { return tools.actionCell(column, direction); }
    String valueCell(Cell cell, DBValue value) { return tools.valueCell(cell, value); }

    Order.Direction direction(DBColumn column) {
        for (Order order : results.select.orders) {
            if (order.column.equals(column)) {
                return order.direction;
            }
        }
        return Order.Direction.NONE;
    }


}

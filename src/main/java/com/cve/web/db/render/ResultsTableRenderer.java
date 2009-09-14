package com.cve.web.db.render;

import com.cve.db.Cell;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.SelectResults;
import com.cve.db.DBTable;
import com.cve.db.Order;
import com.cve.db.Value;
import com.cve.html.CSS;
import com.cve.ui.UIDetail;
import com.cve.ui.UIRow;
import com.cve.ui.UITable;
import com.cve.web.ClientInfo;
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

public final class ResultsTableRenderer {

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
        tools = DBResultSetRenderer.resultsHintsClient(results.resultSet, results.select.orders, results.hints, client);
    }

    static ResultsTableRenderer resultsClientInfo(SelectResults results, ClientInfo client) {
        return new ResultsTableRenderer(results,client);
    }

    public static String render(SelectResults results, ClientInfo client) {
        return new ResultsTableRenderer(results,client).resultsTable();
    }

    static String    tdRowspan(String s, int width) { return "<td rowspan=" + q(width) + ">" + s + "</td>"; }


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
        UIRow headerRow = UIRow.of(
            UIDetail.of("Database",CSS.DATABASE),
            UIDetail.of("Table",CSS.TABLE),
            UIDetail.of("Column",CSS.COLUMN),
            UIDetail.of("Action",CSS.HIDE),
            UIDetail.of("Value"));
        out.add(headerRow);
        Database lastDatabase = Database.NULL;
        DBTable     lastTable = DBTable.NULL;
        for (DBColumn column : resultSet.columns) {
            List<UIDetail> details = Lists.newArrayList();
            DBTable          table = column.table;
            Database      database = table.database;
            if (database.equals(lastDatabase)) {
                details.add(UIDetail.of("",CSS.DATABASE));
            } else {
                details.add(UIDetail.of(nameCell(database),CSS.DATABASE));
                lastDatabase = database;
            }
            if (table.equals(lastTable)) {
                details.add(UIDetail.of("",CSS.TABLE));
            } else {
                details.add(UIDetail.of(nameCell(table),CSS.TABLE));
                lastTable = table;
            }
            details.add(UIDetail.of(nameCell(column),tools.classOf(column)));
            details.add(UIDetail.of(actionCell(column,direction(column)),CSS.HIDE));
            for (DBRow row : resultSet.rows) {
                Cell cell = Cell.at(row, column);
                Value value = resultSet.getValue(row, column);
                details.add(UIDetail.of(valueCell(cell,value)));
            }
            out.add(UIRow.of(details));
        }
        out.add(headerRow);
        return UITable.of(out).toString();
    }

    Order.Direction direction(DBColumn column) {
        for (Order order : results.select.orders) {
            if (order.column.equals(column)) {
                return order.direction;
            }
        }
        return Order.Direction.NONE;
    }

    String nameCell(DBColumn column) {
        return tools.nameCell(column);
    }
  
    String nameCell(Database database) {
        return DBResultSetRenderer.nameCell(database);
    }

    String nameCell(DBTable table) {
        return DBResultSetRenderer.nameCell(table);
    }

}

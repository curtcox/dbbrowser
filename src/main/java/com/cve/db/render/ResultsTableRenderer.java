package com.cve.db.render;

import com.cve.db.Cell;
import com.cve.db.DBColumn;
import com.cve.db.Database;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.SelectResults;
import com.cve.db.DBTable;
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
import static com.cve.db.render.ResultsTableRenderingTools.*;

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
    private final ResultsTableRenderingTools tools;

    private ResultsTableRenderer(SelectResults results, ClientInfo client) {
        this.results = notNull(results);
        this.client  = notNull(client);
        tools = ResultsTableRenderingTools.results(results, client);
    }

    static ResultsTableRenderer results(SelectResults results, ClientInfo client) {
        return new ResultsTableRenderer(results,client);
    }

    static String render(SelectResults results, ClientInfo client) {
        return new ResultsTableRenderer(results,client).resultsTable();
    }

    public static String    tdRowspan(String s, int width) { return "<td rowspan=" + q(width) + ">" + s + "</td>"; }


    String resultsTable() {
        // Tables with few enough rows get printed in portrait
        if (results.resultSet.rows.size() < 4) {
            return portraitTable();
        }
        return landscapeTable();
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
            UIDetail.of("Hide",CSS.HIDE),
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
            details.add(UIDetail.of(hideCell(column),CSS.HIDE));
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

    String nameCell(DBColumn column) {
        return tools.nameCell(column);
    }
  
    String nameCell(Database database) {
        return ResultsTableRenderingTools.nameCell(database);
    }

    String nameCell(DBTable table) {
        return ResultsTableRenderingTools.nameCell(table);
    }

    /**
     * Return a landscape table where every result set row maps to a table row.
     */
    String landscapeTable() {
        List<UIRow> rows = Lists.newArrayList();
        rows.add(UIRow.of(tools.databaseRow(),   CSS.DATABASE));
        rows.add(UIRow.of(tools.tableRow(),      CSS.TABLE));
        rows.add(UIRow.of(tools.columnNameRow()));
        rows.add(UIRow.of(tools.columnHideRow(), CSS.HIDE));
        rows.addAll(valueRows());
        return UITable.of(rows).toString();
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
            for (DBColumn column : resultSet.columns) {
                Cell cell = Cell.at(row, column);
                Value value = resultSet.getValue(row, column);
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

}

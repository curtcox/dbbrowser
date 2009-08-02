package com.cve.web.alt;

import com.cve.db.DBColumn;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.SelectResults;
import com.cve.db.Value;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
final class CSVHandler extends AbstractRequestHandler {

    CSVHandler() { super("^/view/CSV/"); }

    public PageResponse doProduce(PageRequest request) throws IOException, SQLException {
        return csv(AlternateViewHandler.getResultsFromDB(request.getRequestURI()));
    }

    public static PageResponse csv(SelectResults results) {
        DBResultSet  rows = results.getResultSet();
        CSVModelBuilder builder = new CSVModelBuilder();
        int maxColumn = rows.getColumns().size();
        for (DBRow row : rows.getRows()) {
            for (int i=0; i<maxColumn; i++) {
                DBColumn column = rows.getColumns().get(i);
                Value value = rows.getValue(row, column);
                builder.add(value);
            }
            builder.next();
        }
        return PageResponse.of(builder.build());
    }
}

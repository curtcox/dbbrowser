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
import static com.cve.log.Log.args;

/**
 * For handling requests to view a result set as CSV.
 * @author curt
 */
final class CSVHandler extends AbstractRequestHandler {

    CSVHandler() { super("^/view/CSV/"); }

    @Override
    public PageResponse get(PageRequest request) throws IOException, SQLException {
        args(request);
        return csv(AlternateViewHandler.getResultsFromDB(request.requestURI));
    }

    public static PageResponse csv(SelectResults results) {
        args(results);
        DBResultSet  rows = results.resultSet;
        CSVModelBuilder builder = new CSVModelBuilder();
        int maxColumn = rows.columns.size();
        for (DBRow row : rows.rows) {
            for (int i=0; i<maxColumn; i++) {
                DBColumn column = rows.columns.get(i);
                Value value = rows.getValue(row, column);
                builder.add(value);
            }
            builder.next();
        }
        return PageResponse.of(builder.build());
    }
}

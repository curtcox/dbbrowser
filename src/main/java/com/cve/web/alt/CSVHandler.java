package com.cve.web.alt;

import com.cve.db.DBColumn;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.SelectResults;
import com.cve.db.Value;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.Strings;
import com.cve.web.AbstractBinaryRequestHandler;
import com.cve.web.ContentType;
import com.cve.web.PageRequest;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import static com.cve.log.Log.args;

/**
 * For handling requests to view a result set as CSV.
 * @author curt
 */
final class CSVHandler extends AbstractBinaryRequestHandler {

     /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private CSVHandler(DBMetaData.Factory db) {
        super("^/view/CSV/",ContentType.TEXT);
        this.db = db;
    }

    static CSVHandler of(DBMetaData.Factory db) {
        return new CSVHandler(db);
    }

    @Override
    public byte[] get(PageRequest request) throws IOException, SQLException {
        args(request);
        AlternateViewHandler alt = AlternateViewHandler.of(db);
        return csv(alt.getResultsFromDB(request.requestURI));
    }

    public static byte[] csv(SelectResults results) {
        args(results);
        DBResultSet  rows = results.resultSet;
        StringBuilder out = new StringBuilder();
        int maxColumn = rows.columns.size();
        for (DBRow row : rows.rows) {
            List<String> values = Lists.newArrayList();
            for (int i=0; i<maxColumn; i++) {
                DBColumn column = rows.columns.get(i);
                Value value = rows.getValue(row, column);
                values.add(value.toString());
            }
            out.append(Strings.separated(values, ",") + "\r\n");
        }
        return out.toString().getBytes();
    }
}

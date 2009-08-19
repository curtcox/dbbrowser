package com.cve.web.alt;

import com.cve.db.DBColumn;
import com.cve.db.Hints;
import com.cve.db.DBResultSet;
import com.cve.db.DBRow;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.Value;
import com.cve.db.dbio.DBConnection;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.HintsStore;
import com.cve.stores.ServersStore;
import com.cve.util.URIParser;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import java.io.IOException;
import java.sql.SQLException;


/**
 *
 * @author curt
 */
final class SQLHandler extends AbstractRequestHandler {

    SQLHandler() { super("^/view/SQL/");}

    public PageResponse get(PageRequest request) throws IOException, SQLException {
        return rows(AlternateViewHandler.getResultsFromDB(request.requestURI));
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    static SelectResults getResultsFromDB(String uri) throws SQLException {
        // The server out of the URL
        Server         server = URIParser.getServer(uri);

        // Setup the select
        Select           select = URIParser.getSelect(uri);
        DBConnection connection = ServersStore.getConnection(server);
        Hints hints = HintsStore.getHints(select.columns);

        // run the select
        SelectResults results = SelectExecutor.run(
            select, server, connection, hints);
        return results;
    }

    public static PageResponse rows(SelectResults results) {
        DBResultSet  rows = results.resultSet;
        SQLModelBuilder builder = new SQLModelBuilder();
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

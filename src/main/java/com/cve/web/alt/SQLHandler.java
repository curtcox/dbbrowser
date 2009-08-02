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
import com.cve.db.select.SelectRunner;
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

    public PageResponse doProduce(PageRequest request) throws IOException, SQLException {
        return rows(AlternateViewHandler.getResultsFromDB(request.getRequestURI()));
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
        Hints hints = HintsStore.getHints(select.getColumns());

        // run the select
        SelectResults results = SelectRunner.run(
            select, server, connection, hints);
        return results;
    }

    public static PageResponse rows(SelectResults results) {
        DBResultSet  rows = results.getResultSet();
        SQLModelBuilder builder = new SQLModelBuilder();
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

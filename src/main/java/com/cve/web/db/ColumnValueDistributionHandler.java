
package com.cve.web.db;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Group;
import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.HintsStore;
import com.cve.stores.ServersStore;
import com.cve.util.URIs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
final class ColumnValueDistributionHandler extends AbstractRequestHandler {

    ColumnValueDistributionHandler() {}

    @Override
    public SelectResults get(PageRequest request) throws IOException, SQLException {
        String uri = request.requestURI;
        return  getResultsFromDB(uri);
    }

    @Override
    public boolean handles(String uri) {
        return isColumnValueDist(uri);
    }

    /**
     * Is uri of the form /server/database/table/column[/]
     * @param uri
     * @return
     */
    static boolean isColumnValueDist(String uri) {
        if (URIs.slashCount(uri)!=4 && URIs.slashCount(uri)!=5) {
            return false;
        }
        if (DBURIParser.getDatabases(uri).size() !=1){
            return false;
        }
        ImmutableList<DBTable> tables = DBURIParser.getTables(uri);
        if (tables.size()!=1){
            return false;
        }
        if (DBURIParser.getColumns(tables, uri).size()!=1) {
            return false;
        }
        return true;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    static SelectResults getResultsFromDB(String uri) throws SQLException {
        // The server out of the URL
        Server         server = DBURIParser.getServer(uri);

        // Setup the select
        Select           select = DBURIParser.getSelect(uri);
        DBColumn column = select.columns.get(0);
        select = select.with(column, AggregateFunction.COUNT);
        select = select.with(Group.of(column));
        DBConnection connection = ServersStore.getConnection(server);
        Hints hints = HintsStore.getHints(select.columns);

        // run the select
        SelectResults results = SelectExecutor.run(select, server, connection, hints);
        return results;
    }
}

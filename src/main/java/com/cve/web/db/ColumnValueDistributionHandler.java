
package com.cve.web.db;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Group;
import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectContext;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBMetaData;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.HintsStore;
import com.cve.stores.Stores;
import com.cve.util.URIs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.PageRequest;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author curt
 */
final class ColumnValueDistributionHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private ColumnValueDistributionHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    static ColumnValueDistributionHandler of(DBMetaData.Factory db) {
        return new ColumnValueDistributionHandler(db);
    }

    @Override
    public SelectResults get(PageRequest request) throws IOException, SQLException {
        String uri = request.requestURI;
        return  getResultsFromDB(uri);
    }

    @Override
    public boolean handles(String uri) {
        return isColumnValueDist(uri);
    }

    /**                   1  2     3        4     5       6
     * Is uri of the form /+/server/database/table/column[/]
     * @param uri
     * @return
     */
    static boolean isColumnValueDist(String uri) {
        int slashes = URIs.slashCount(uri);
        if (slashes!=5 && slashes!=6) {
            return false;
        }
        if (DBURICodec.getDatabases(uri).size() !=1){
            return false;
        }
        ImmutableList<DBTable> tables = DBURICodec.getTables(uri);
        if (tables.size()!=1){
            return false;
        }
        if (DBURICodec.getColumns(tables, uri).size()!=1) {
            return false;
        }
        return true;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    SelectResults getResultsFromDB(String uri) throws SQLException {
        // The server out of the URL
        Server         server = DBURICodec.getServer(uri);

        // Setup the select
        Select           select = DBURICodec.getSelect(uri);
        DBColumn column = select.columns.get(0);
        select = select.with(column, AggregateFunction.COUNT);
        select = select.with(Group.of(column));
        DBConnection connection = Stores.getServerStores().getConnection(server);
        Hints hints = HintsStore.of(db).getHints(select.columns);

        // run the select
        SelectContext context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults results = SelectExecutor.run(context);
        return results;
    }
}

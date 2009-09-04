package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.dbio.DBConnection;
import com.cve.db.DBColumn;
import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.SelectContext;
import com.cve.db.dbio.DBMetaData;
import com.cve.db.select.SelectExecutor;
import com.cve.db.select.URIRenderer;
import com.cve.stores.HintsStore;
import com.cve.stores.ServersStore;
import com.cve.util.URIs;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import static com.cve.log.Log.args;


/**
 * Build a page that renders the results from a select statement.
 * Additionally, the page will contain links to pages for other
 * select statements.  It can be used to iteratively build a complex
 * select statement.
 */
public final class SelectBuilderHandler implements RequestHandler {

    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        args(request);
        PageResponse redirect = redirectedWithAddedColumns(request);
        if (redirect!=null) {
            return redirect;
        }

        String uri = request.requestURI;
        if (!isSelectBuilderRequest(uri)) {
            return null;
        }
        SelectResults results = getResultsFromDB(uri);
        return PageResponse.of(results);
    }

    /**
     * Requests with no columns are ambiguous.  Do they mean all or none?
     * If we get such a request, we construct a URL with all columns and
     * redirect to it.
     * <p>
     * Returns a PageResponse if this page should be redirected, or null
     * otherwise.
     */
    static PageResponse redirectedWithAddedColumns(PageRequest request) throws SQLException {
        String    uri = request.requestURI;
        Select select = DBURICodec.getSelect(uri);
        if (select.columns.size()>0) {
            return null;
        }
        // We've determined this page needs redirected.
        // Now figure out where to.
        for (DBTable table : select.tables) {
            DBMetaData meta = DBConnection.getDbmd(table.database.server);
            for (DBColumn column : meta.getColumnsFor(table)) {
                select = select.with(column);
            }
        }
        Search search = DBURICodec.getSearch(uri);
        URI dest = URIRenderer.render(select,search);
        return PageResponse.newRedirect(dest);
    }

    /**
     * Return true, if uri string like:
     * /server/dbs/tables/columns...
     */
    static boolean isSelectBuilderRequest(String uri) {
        return URIs.slashCount(uri)>3;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    static SelectResults getResultsFromDB(String uri) throws SQLException {
        // The server out of the URL
        Server         server = DBURICodec.getServer(uri);

        // Setup the select
        Select           select = DBURICodec.getSelect(uri);
        DBConnection connection = ServersStore.getConnection(server);
        Hints hints = HintsStore.getHints(select.columns);

        SelectContext context = SelectContext.of(select, Search.EMPTY, server, connection, hints);

        // run the select
        SelectResults results = SelectExecutor.run(context);
        return results;
    }

}

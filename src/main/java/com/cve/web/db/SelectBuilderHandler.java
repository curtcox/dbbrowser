package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.dbio.DBConnection;
import com.cve.db.DBColumn;
import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBMetaData;
import com.cve.db.select.SelectRunner;
import com.cve.db.select.URIRenderer;
import com.cve.stores.HintsStore;
import com.cve.stores.ServersStore;
import com.cve.util.URIParser;
import com.cve.util.URIs;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;


/**
 * Build a page that renders the results from a select statement.
 * Additionally, the page will contain links to pages for other
 * select statements.  It can be used to iteratively build a complex
 * select statement.
 */
public final class SelectBuilderHandler implements RequestHandler {

    public PageResponse produce(PageRequest request) throws IOException, SQLException {

        PageResponse redirect = redirectedWithAddedColumns(request);
        if (redirect!=null) {
            return redirect;
        }

        String uri = request.getRequestURI();
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
        String    uri = request.getRequestURI();
        Select select = URIParser.getSelect(uri);
        if (select.getColumns().size()>0) {
            return null;
        }
        // We've determined this page needs redirected.
        // Now figure out where to.
        for (DBTable table : select.getTables()) {
            DBMetaData meta = DBConnection.getDbmd(table.getDatabase().getServer());
            for (DBColumn column : meta.getColumnsFor(table)) {
                select = select.with(column);
            }
        }
        URI dest = URIRenderer.render(select);
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

}

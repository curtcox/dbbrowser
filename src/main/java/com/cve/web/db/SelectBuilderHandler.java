package com.cve.web.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.web.*;
import com.cve.io.db.DBConnection;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Hints;
import com.cve.model.db.Select;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.model.db.SelectContext;
import com.cve.io.db.DBConnectionFactory;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.select.SelectExecutor;
import com.cve.io.db.select.URIRenderer;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.DBHintsStore;
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

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final DBHintsStore hintsStore;

    private SelectBuilderHandler(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore) {
        this.db = db;
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
    }

    static SelectBuilderHandler of(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore) {
        return new SelectBuilderHandler(db,serversStore,hintsStore);
    }

    @Override
    public PageResponse produce(PageRequest request) {
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
    PageResponse redirectedWithAddedColumns(PageRequest request) {
        String    uri = request.requestURI;
        //  1     2      3
        // /search/server/db
        if (URIs.slashCount(uri)<3) {
            return null;
        }
        Select select = DBURICodec.getSelect(uri);
        if (select.columns.size()>0) {
            return null;
        }
        // We've determined this page needs redirected.
        // Now figure out where to.
        for (DBTable table : select.tables) {
            DBMetaData meta = db.of(table.database.server);
            for (DBColumn column : meta.getColumnsFor(table).value) {
                select = select.with(column);
            }
        }
        Search search = DBURICodec.getSearch(uri);
        URI dest = URIRenderer.render(select,search);
        return PageResponse.newRedirect(dest);
    }

    /**
     * Return true, if uri string like:
     * 1      2      3   4      5
     * /search/server/dbs/tables/columns...
     */
    static boolean isSelectBuilderRequest(String uri) {
        return URIs.slashCount(uri)>4;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    SelectResults getResultsFromDB(String uri) {
        // The server out of the URL
        DBServer         server = DBURICodec.getServer(uri);

        // Setup the select
        Select           select = DBURICodec.getSelect(uri);
        Search           search = DBURICodec.getSearch(uri);
        DBConnection connection = DBConnectionFactory.getConnection(server, serversStore, null);
        Hints             hints = hintsStore.get(select.columns);

        SelectContext context = SelectContext.of(select, search, server, connection, hints);

        // run the select
        SelectResults results = SelectExecutor.run(context);
        return results;
    }

}

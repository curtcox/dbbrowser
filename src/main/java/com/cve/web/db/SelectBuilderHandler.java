package com.cve.web.db;

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
import com.cve.io.db.select.DBURIRenderer;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.DBHintsStore;
import com.cve.util.URIs;
import java.net.URI;
import static com.cve.util.Check.notNull;

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

    final ManagedFunction.Factory managedFunction;

    final Log log = Logs.of();

    final DBURICodec codec;

    final DBConnectionFactory connections;

    private SelectBuilderHandler(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        this.db = notNull(db);
        this.serversStore = notNull(serversStore);
        this.hintsStore = notNull(hintsStore);
        this.managedFunction = notNull(managedFunction);
        codec = DBURICodec.of();
        connections = DBConnectionFactory.of(serversStore, managedFunction);
    }

    static SelectBuilderHandler of(
         DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
         ManagedFunction.Factory managedFunction)
    {
        return new SelectBuilderHandler(db,serversStore,hintsStore,managedFunction);
    }

    @Override
    public PageResponse produce(PageRequest request) {
        log.args(request);
        PageResponse redirect = redirectedWithAddedColumns(request);
        if (redirect!=null) {
            return redirect;
        }

        String uri = request.requestURI;
        if (!isSelectBuilderRequest(uri)) {
            return null;
        }
        SelectResults results = getResultsFromDB(uri);
        return PageResponse.of(request,results);
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
        Select select = codec.getSelect(uri);
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
        Search search = codec.getSearch(uri);
        URI dest = DBURIRenderer.render(select,search);
        return PageResponse.newRedirect(request,dest);
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
        DBServer         server = codec.getServer(uri);

        // Setup the select
        Select           select = codec.getSelect(uri);
        Search           search = codec.getSearch(uri);
        DBConnection connection = connections.getConnection(server);
        Hints             hints = hintsStore.get(select.columns);

        SelectContext context = SelectContext.of(select, search, server, connection, hints);

        // run the select
        SelectResults results = SelectExecutor.of().run(context);
        return results;
    }

}

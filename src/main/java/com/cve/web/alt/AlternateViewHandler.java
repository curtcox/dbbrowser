package com.cve.web.alt;

import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectContext;
import com.cve.db.SelectResults;
import com.cve.db.DBServer;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBConnectionFactory;
import com.cve.db.dbio.DBMetaData;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.db.DBHintsStore;
import com.cve.web.PageRequest;
import com.cve.web.PageResponse;
import com.cve.web.db.DBURICodec;
import com.cve.util.URIs;
import com.cve.web.CompositeRequestHandler;
import com.cve.web.RequestHandler;
import com.cve.web.Search;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * The {@link RequestHandler} for alternate views of a select result.
 */
public final class AlternateViewHandler implements RequestHandler {

    private final DBServersStore serversStore;
    private final DBHintsStore hintsStore;
    private final ManagedFunction.Factory managedFunction;
    private final RequestHandler handler;

    private AlternateViewHandler(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
        handler = CompositeRequestHandler.of(
            // handler            // for URLs of the form
            CSVHandler.of(db,serversStore,hintsStore,managedFunction),      // /view/CSV/
            new XLSHandler(),     // /view/XLS/
            new PDFHandler(),     // /view/PDF/
            new JSONHandler(),    // /view/JSON/
            new XMLHandler()  // /view/XML/
        );
    }

    public static AlternateViewHandler of(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new AlternateViewHandler(db,serversStore,hintsStore,managedFunction);
    }


    /**
     * Return the results of the select that corresponds to the given URI.
     */
    SelectResults getResultsFromDB(final String uri) throws SQLException {
        args(uri);
        // /view/CSV/foo
        //          ^ start here
        URI tail = URIs.startingAtSlash(uri,3);

        // The server out of the URL
        DBServer         server = DBURICodec.getServer(tail.toString());

        // Setup the select
        Select           select = DBURICodec.getSelect(tail.toString());
        DBConnection connection = DBConnectionFactory.getConnection(server,serversStore,managedFunction);
        Hints             hints = hintsStore.get(select.columns);

        // run the select
        SelectContext context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults results = SelectExecutor.run(context);
        return results;
    }

    @Override
    public PageResponse produce(PageRequest request) throws IOException, SQLException {
        return handler.produce(request);
    }
}

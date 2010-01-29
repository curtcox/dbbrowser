package com.cve.web.alt;

import com.cve.model.db.Hints;
import com.cve.model.db.Select;
import com.cve.model.db.SelectContext;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBConnection;
import com.cve.io.db.DBConnectionFactory;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.select.SelectExecutor;
import com.cve.log.Log;
import com.cve.log.Logs;
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
import java.net.URI;
import static com.cve.util.Check.notNull;

/**
 * The {@link RequestHandler} for alternate views of a select result.
 */
public final class AlternateViewHandler implements RequestHandler {

    private final DBServersStore serversStore;
    private final DBHintsStore hintsStore;
    private final ManagedFunction.Factory managedFunction;
    private final RequestHandler handler;
    private final DBURICodec codec;
    private final DBConnectionFactory connections;
    private final Log log = Logs.of();

    private AlternateViewHandler(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        this.serversStore = notNull(serversStore);
        this.hintsStore = notNull(hintsStore);
        this.managedFunction = notNull(managedFunction);
        
        handler = CompositeRequestHandler.of(
            // handler            // for URLs of the form
            CSVHandler.of(db,serversStore,hintsStore,managedFunction),      // /view/CSV/
            XLSHandler.of(),     // /view/XLS/
            PDFHandler.of(),     // /view/PDF/
            JSONHandler.of(),    // /view/JSON/
            XMLHandler.of()  // /view/XML/
        );
        codec = DBURICodec.of();
        connections = DBConnectionFactory.of(serversStore, managedFunction);
    }

    public static AlternateViewHandler of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new AlternateViewHandler(db,serversStore,hintsStore,managedFunction);
    }


    /**
     * Return the results of the select that corresponds to the given URI.
     */
    SelectResults getResultsFromDB(final String uri) {
        log.args(uri);
        // /view/CSV/foo
        //          ^ start here
        URI tail = URIs.startingAtSlash(uri,3);

        // The server out of the URL
        DBServer         server = codec.getServer(tail.toString());

        // Setup the select
        Select           select = codec.getSelect(tail.toString());
        DBConnection connection = connections.getConnection(server);
        Hints             hints = hintsStore.get(select.columns);

        // run the select
        SelectContext context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults results = SelectExecutor.of().run(context);
        return results;
    }

    @Override
    public PageResponse produce(PageRequest request) {
        return handler.produce(request);
    }
}

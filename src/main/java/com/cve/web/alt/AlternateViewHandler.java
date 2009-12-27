package com.cve.web.alt;

import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectContext;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBMetaData;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.HintsStore;
import com.cve.stores.ServersStore;
import com.cve.stores.Stores;
import com.cve.web.db.DBURICodec;
import com.cve.util.URIs;
import com.cve.web.CompositeRequestHandler;
import com.cve.web.RequestHandler;
import com.cve.web.Search;
import java.net.URI;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * The {@link RequestHandler} for alternate views of a select result.
 */
public final class AlternateViewHandler implements RequestHandler.Factory {

     /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private AlternateViewHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    public static AlternateViewHandler of(DBMetaData.Factory db) {
        return new AlternateViewHandler(db);
    }


    public RequestHandler of() {
        return CompositeRequestHandler.of(
            // handler            // for URLs of the form
            CSVHandler.of(db),      // /view/CSV/
            new XLSHandler(),     // /view/XLS/
            new PDFHandler(),     // /view/PDF/
            new JSONHandler(),    // /view/JSON/
            new XMLHandler()  // /view/XML/
        );
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
        Server         server = DBURICodec.getServer(tail.toString());

        // Setup the select
        Select           select = DBURICodec.getSelect(tail.toString());
        DBConnection connection = Stores.getServerStores().getConnection(server);
        Hints             hints = HintsStore.of(db).getHints(select.columns);

        // run the select
        SelectContext context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults results = SelectExecutor.run(context);
        return results;
    }
}

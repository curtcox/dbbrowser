package com.cve.web.alt;

import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.select.SelectExecutor;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.HintsStore;
import com.cve.stores.ServersStore;
import com.cve.util.URIParser;
import com.cve.util.URIs;
import com.cve.web.CompositeRequestHandler;
import com.cve.web.RequestHandler;
import java.net.URI;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * The {@link RequestHandler} for alternate views of a select result.
 */
public class AlternateViewHandler {

    private static final RequestHandler handler = CompositeRequestHandler.of(
        // handler            // for URLs of the form
        new CSVHandler(),     // /view/CSV/
        new XLSHandler(),     // /view/XLS/
        new PDFHandler(),     // /view/PDF/
        new JSONHandler(),    // /view/JSON/
        new XMLHandler());    // /view/XML/

    public static RequestHandler newInstance() {
        return handler;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    static SelectResults getResultsFromDB(final String uri) throws SQLException {
        args(uri);
        // /view/CSV/foo
        //          ^ start here
        URI tail = URIs.startingAtSlash(uri,3);

        // The server out of the URL
        Server         server = URIParser.getServer(tail.toString());

        // Setup the select
        Select           select = URIParser.getSelect(tail.toString());
        DBConnection connection = ServersStore.getConnection(server);
        Hints hints = HintsStore.getHints(select.columns);

        // run the select
        SelectResults results = SelectExecutor.run(
            select, server, connection, hints);
        return results;
    }
}

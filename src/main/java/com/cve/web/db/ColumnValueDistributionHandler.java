
package com.cve.web.db;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Group;
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
import com.cve.util.URIs;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.PageRequest;
import com.cve.web.core.Search;
import com.google.common.collect.ImmutableList;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
final class ColumnValueDistributionHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final DBHintsStore hintsStore;

    final ManagedFunction.Factory managedFunction;

    final DBURICodec codec;

    final Log log = Logs.of();

    final DBConnectionFactory connections;

    private ColumnValueDistributionHandler(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        super();
        this.db = notNull(db);
        this.serversStore = notNull(serversStore);
        this.hintsStore = notNull(hintsStore);
        this.managedFunction = notNull(managedFunction);
        
        connections = DBConnectionFactory.of(serversStore, managedFunction);
        codec = DBURICodec.of();
    }

    static ColumnValueDistributionHandler of(
        DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore,
        ManagedFunction.Factory managedFunction)
    {
        return new ColumnValueDistributionHandler(db,serversStore,hintsStore, managedFunction);
    }

    @Override
    public SelectResults get(PageRequest request) {
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
    boolean isColumnValueDist(String uri) {
        int slashes = URIs.slashCount(uri);
        if (slashes!=5 && slashes!=6) {
            return false;
        }
        if (codec.getDatabases(uri).size() !=1){
            return false;
        }
        ImmutableList<DBTable> tables = codec.getTables(uri);
        if (tables.size()!=1){
            return false;
        }
        if (codec.getColumns(tables, uri).size()!=1) {
            return false;
        }
        return true;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    SelectResults getResultsFromDB(String uri) {
        // The server out of the URL
        DBServer         server = codec.getServer(uri);

        // Setup the select
        Select           select = codec.getSelect(uri);
        DBColumn column = select.columns.get(0);
        select = select.with(column, AggregateFunction.COUNT);
        select = select.with(Group.of(column));
        DBConnection connection = connections.getConnection(server);
        Hints hints = hintsStore.get(select.columns);

        // run the select
        SelectContext context = SelectContext.of(select, Search.EMPTY, server, connection, hints);
        SelectResults results = SelectExecutor.of().run(context);
        return results;
    }
}

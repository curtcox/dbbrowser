package com.cve.web.db;

import com.cve.web.db.databases.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
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
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.web.core.Search;
import com.google.common.collect.Lists;
import java.util.List;
/**
 * For creating a database contents search page.
 * @author curt
 */
final class DatabaseContentsSearchPageCreator {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final DBHintsStore hintsStore;

    final ManagedFunction.Factory managedFunction;

    final DBConnectionFactory connections;

    final Log log = Logs.of();

    private DatabaseContentsSearchPageCreator(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        this.db = db;
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
        
        connections = DBConnectionFactory.of(serversStore, managedFunction);
    }

    static DatabaseContentsSearchPageCreator of(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new DatabaseContentsSearchPageCreator(db,serversStore,hintsStore, managedFunction);
    }

    public DatabaseContentsSearchPage create(Database database, Search search) {
        List<SelectResults> resultsList = createResultsList(database,search);
        return DatabaseContentsSearchPage.of(search, database, resultsList);
    }

    List<SelectResults> createResultsList(Database database, Search search) {
        log.args(database,search);
        List<SelectResults> resultsList = Lists.newArrayList();
        DBMetaData                 meta = db.of(database.server);
        for (DBTable table : meta.getTablesOn(database).value) {
            SelectResults results = getResultsFromTable(meta,search,table);
            if (!results.resultSet.rows.isEmpty()) {
                resultsList.add(results);
            }
        }
        return resultsList;
    }

    /**
     * Return the results of the select that corresponds to the given URI.
     */
    SelectResults getResultsFromTable(DBMetaData meta ,Search search, DBTable table) {
        DBServer         server = table.database.server;

        // Setup the select
        Database database = table.database;
        DBColumn[] columns = meta.getColumnsFor(table).value.toArray(new DBColumn[0]);
        Select           select = Select.from(database,table,columns);
        DBConnection connection = connections.getConnection(server);
        Hints hints = hintsStore.get(select.columns);

        SelectContext context = SelectContext.of(select, search, server, connection, hints);

        // run the select
        SelectResults results = SelectExecutor.of().run(context);
        return results;
    }
}

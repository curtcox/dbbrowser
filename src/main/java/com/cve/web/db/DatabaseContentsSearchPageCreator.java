package com.cve.web.db;

import com.cve.web.db.databases.*;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectContext;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.DBConnectionFactory;
import com.cve.db.dbio.DBMetaData;
import com.cve.db.select.SelectExecutor;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.HintsStore;
import com.cve.stores.db.ServersStore;
import com.cve.web.Search;
import com.google.common.collect.Lists;
import java.sql.SQLException;
import java.util.List;
import static com.cve.log.Log.args;
/**
 * For creating a database contents search page.
 * @author curt
 */
final class DatabaseContentsSearchPageCreator {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final ServersStore serversStore;

    final HintsStore hintsStore;

    final ManagedFunction.Factory managedFunction;

    private DatabaseContentsSearchPageCreator(DBMetaData.Factory db, ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        this.db = db;
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
    }

    static DatabaseContentsSearchPageCreator of(DBMetaData.Factory db, ServersStore serversStore, HintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new DatabaseContentsSearchPageCreator(db,serversStore,hintsStore, managedFunction);
    }

    public DatabaseContentsSearchPage create(Database database, Search search) throws SQLException {
        List<SelectResults> resultsList = createResultsList(database,search);
        return DatabaseContentsSearchPage.of(search, database, resultsList);
    }

    List<SelectResults> createResultsList(Database database, Search search) throws SQLException {
        args(database,search);
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
    SelectResults getResultsFromTable(DBMetaData meta ,Search search, DBTable table) throws SQLException {
        Server         server = table.database.server;

        // Setup the select
        Database database = table.database;
        DBColumn[] columns = meta.getColumnsFor(table).value.toArray(new DBColumn[0]);
        Select           select = Select.from(database,table,columns);
        DBConnection connection = DBConnectionFactory.getConnection(server,serversStore,managedFunction);
        Hints hints = hintsStore.getHints(select.columns);

        SelectContext context = SelectContext.of(select, search, server, connection, hints);

        // run the select
        SelectResults results = SelectExecutor.run(context);
        return results;
    }
}

package com.cve.web.db;

import com.cve.web.*;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import com.cve.web.Search.Space;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import static com.cve.log.Log.args;
/**
 * For picking a table.
 */
public final class TablesHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final DBHintsStore hintsStore;

    final ManagedFunction.Factory managedFunction;

    private TablesHandler(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        this.db = db;
        this.serversStore = serversStore;
        this.hintsStore = hintsStore;
        this.managedFunction = managedFunction;
    }

    static TablesHandler of(DBMetaData.Factory db, DBServersStore serversStore, DBHintsStore hintsStore, ManagedFunction.Factory managedFunction) {
        return new TablesHandler(db,serversStore,hintsStore,managedFunction);
    }
    
    /**
     * Do we handle this URI?
     */
    @Override
    public boolean handles(String uri) {
        return isTablesOnlyRequest(uri);
    }

    /**
     * Get the response (list of tables) for this request.
     */
    @Override
    public Model get(PageRequest request) {
        args(request);
        String                    uri = request.requestURI;
        Search                 search = DBURICodec.getSearch(uri);
        DBServer                 server = DBURICodec.getServer(uri);
        Database             database = DBURICodec.getDatabase(uri);
        if (search.isEmpty()) {
            DBMetaData               meta = db.of(server);
            ImmutableList<DBTable> tables = meta.getTablesOn(database).value;
            ImmutableMultimap<DBTable,DBColumn> columns = columnsFor(tables);
            ImmutableMap<DBTable,Long>             rows = rowsFor(tables);
            return new TablesPage(server,database,tables,rows,columns);
        }
        if (search.space==Space.CONTENTS) {
            return DatabaseContentsSearchPageCreator.of(db,serversStore,hintsStore,managedFunction).create(database,search);
        }
        return newNamesSearchPage(database,search);
    }

    /**
     * Return true if URL is of the form
     * /+/server/db/
     */
    public static boolean isTablesOnlyRequest(String uri) {
        return URIs.slashCount(uri)==4;
    }

    /**
     * Return a map from the given tables to the columns they contain.
     */
    ImmutableMultimap<DBTable,DBColumn> columnsFor(ImmutableList<DBTable> tables) {
        Multimap<DBTable,DBColumn> columns = HashMultimap.create();
        for (DBTable table : tables) {
            DBServer      server = table.database.server;
            DBMetaData    meta = db.of(server);
            for (DBColumn column : meta.getColumnsFor(table).value) {
                columns.put(table, column);
            }
        }
        return ImmutableMultimap.copyOf(columns);
    }

        /**
     * Return a map from the given tables to the columns they contain.
     */
    ImmutableMap<DBTable,Long> rowsFor(ImmutableList<DBTable> tables) {
        Map<DBTable,Long> rows = Maps.newHashMap();
        for (DBTable table : tables) {
            DBServer      server = table.database.server;
            DBMetaData    meta = db.of(server);
            rows.put(table, meta.getRowCountFor(table).value);
        }
        return ImmutableMap.copyOf(rows);
    }

    /**
     * Perform the requested search of the table, column, database
     * and server names.  Return a results page.
     */
    TablesSearchPage newNamesSearchPage(Database database,Search search) {
        args(database,search);
        DBMetaData               meta = db.of(database.server);
        ImmutableList<DBColumn> columns = meta.getColumnsFor(database).value;
        Set<DBTable> filteredTables = Sets.newHashSet();
        Multimap<DBTable,DBColumn> filteredColumns = HashMultimap.create();
        for (DBColumn column : columns) {
            String     target = search.target;
            DBTable     table = column.table;
            if (isMatch(column.name,target)) {
                filteredTables.add(table);
                filteredColumns.put(table,column);
            }
            if (isMatch(table.name,target)) {
                filteredTables.add(table);
            }
        }
        return new TablesSearchPage(search, database,
            new ArrayList(filteredTables),filteredColumns);
    }

    /**
     * Return true if the name search should consider this a match.
     */
    static boolean isMatch(String text, String target) {
        return text.toUpperCase().contains(target.toUpperCase());
    }

}

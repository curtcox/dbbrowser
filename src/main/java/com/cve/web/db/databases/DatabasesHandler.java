package com.cve.web.db.databases;

import com.cve.web.core.Model;
import com.cve.web.core.Search;
import com.cve.web.core.PageRequest;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.db.*;
import com.cve.model.db.DBColumn;
import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;
import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.URIs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Set;
import static com.cve.util.Check.notNull;

/**
 * For picking a database.
 * This page lists all of the databases on the specified server.
 */
public final class DatabasesHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBURICodec codec;

    final Log log = Logs.of();

    private DatabasesHandler(DBMetaData.Factory db) {
        super();
        this.db = notNull(db);
        
        codec = DBURICodec.of();
    }

    public static DatabasesHandler of(DBMetaData.Factory db) {
        return new DatabasesHandler(db);
    }

    @Override
    public Model get(PageRequest request) {
        log.args(request);
        String uri = request.requestURI;

        Search search = codec.getSearch(uri);
        DBServer server = codec.getServer(uri);
        if (search.isEmpty()) {
            DBMetaData  meta = db.of(server);
            ImmutableList<Database> databases = meta.getDatabasesOn(server).value;
            ImmutableMultimap<Database,DBTable> tables = tablesOn(databases);
            return new DatabasesPage(server,databases,tables);
        }
        return newSearchPage(server,search);
    }

    @Override
    public boolean handles(String uri) {
        return isDatabaseListRequest(uri);
    }

    /**
     * Return true, if of the form
     * /search/server/
     */
    boolean isDatabaseListRequest(String uri) {
        return URIs.slashCount(uri) > 1 &&
               codec.getServer(uri)!=null &&
               codec.getDatabases(uri).isEmpty();
    }

    ImmutableMultimap<Database,DBTable> tablesOn(ImmutableList<Database> databases) {
        Multimap<Database,DBTable> tables = HashMultimap.create();
        for (Database database : databases) {
            DBMetaData  meta = db.of(database.server);
            for (DBTable table : meta.getTablesOn(database).value) {
                tables.put(database,table);
            }
        }
        return ImmutableMultimap.copyOf(tables);
    }

    /**
     * Perform the requested search and return a results page.
     */
    DatabasesSearchPage newSearchPage(DBServer server,Search search) {
        log.args(server,search);
        ImmutableList<DBColumn> columns = db.of(server).getColumnsFor(server).value;
        Set<Database> filteredDatabases = Sets.newHashSet();
        Multimap<Database,DBTable> filteredTables = HashMultimap.create();
        Multimap<DBTable,DBColumn> filteredColumns = HashMultimap.create();
        String     target = search.target;
        for (DBColumn column : columns) {
            DBTable     table = column.table;
            Database database = table.database;
            if (isMatch(column.name,target)) {
                filteredDatabases.add(database);
                filteredTables.put(database,table);
                filteredColumns.put(table,column);
            }
            if (isMatch(table.name,target)) {
                filteredDatabases.add(database);
                filteredTables.put(database,table);
            }
            if (isMatch(database.name,target)) {
                filteredDatabases.add(database);
            }
        }
        return new DatabasesSearchPage(search, server,
            new ArrayList(filteredDatabases),
            filteredTables,filteredColumns);
    }

    /**
     * Return true if the search should consider this a match.
     */
    static boolean isMatch(String text, String target) {
        return text.toUpperCase().contains(target.toUpperCase());
    }
}

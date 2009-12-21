package com.cve.web.db.databases;

import com.cve.web.db.*;
import com.cve.db.DBColumn;
import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.DBTable;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.URIs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Set;
import static com.cve.log.Log.args;

/**
 * For picking a database.
 * This page lists all of the databases on the specified server.
 */
public final class DatabasesHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private DatabasesHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    public static DatabasesHandler of(DBMetaData.Factory db) {
        return new DatabasesHandler(db);
    }

    @Override
    public Model get(PageRequest request) throws IOException, SQLException {
        args(request);
        String uri = request.requestURI;

        Search search = DBURICodec.getSearch(uri);
        Server server = DBURICodec.getServer(uri);
        if (search.isEmpty()) {
            DBMetaData  meta = db.of(server);
            ImmutableList<Database> databases = meta.getDatabasesOn(server);
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
    static boolean isDatabaseListRequest(String uri) {
        return URIs.slashCount(uri) > 1 &&
               DBURICodec.getServer(uri)!=null &&
               DBURICodec.getDatabases(uri).isEmpty();
    }

    ImmutableMultimap<Database,DBTable> tablesOn(ImmutableList<Database> databases) throws SQLException {
        Multimap<Database,DBTable> tables = HashMultimap.create();
        for (Database database : databases) {
            DBMetaData  meta = db.of(database.server);
            for (DBTable table : meta.getTablesOn(database)) {
                tables.put(database,table);
            }
        }
        return ImmutableMultimap.copyOf(tables);
    }

    /**
     * Perform the requested search and return a results page.
     */
    DatabasesSearchPage newSearchPage(Server server,Search search) throws SQLException {
        args(server,search);
        ImmutableList<DBColumn> columns = db.of(server).getColumnsFor(server);
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

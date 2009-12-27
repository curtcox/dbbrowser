package com.cve.web.db.servers;

import com.cve.web.db.*;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.dbio.DBMetaData;
import com.cve.log.Log;
import com.cve.stores.Stores;
import com.cve.util.URIs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static com.cve.log.Log.args;

/**
 * For picking a database server.
 */
final class ServersHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    private static final Log log = Log.of(ServersHandler.class);

    private ServersHandler(DBMetaData.Factory db) {
        this.db = db;
    }

    static ServersHandler of(DBMetaData.Factory db) {
        return new ServersHandler(db);
    }

    @Override
    public Model get(PageRequest request) throws IOException, SQLException {
        args(request);
        Search search = DBURICodec.getSearch(request.requestURI);
        if (search.isEmpty()) {
            ImmutableList<Server> servers = Stores.getServerStores().getServers();
            ImmutableMultimap<Server,Object> databases = getDatabases(servers);
            return new ServersPage(servers,databases);
        }
        return newSearchPage(search);
    }

    /**
     * Perform the requested search and return a results page.
     */
    ServersSearchPage newSearchPage(Search search) throws SQLException {
        args(search);
        ImmutableList<DBColumn> columns = allColumns();
        Set<Server> filteredServers = Sets.newHashSet();
        Multimap<Server,Database> filteredDatabases = HashMultimap.create();
        Multimap<Database,DBTable> filteredTables = HashMultimap.create();
        Multimap<DBTable,DBColumn> filteredColumns = HashMultimap.create();
        String     target = search.target;
        for (DBColumn column : columns) {
            DBTable     table = column.table;
            Database database = table.database;
            Server     server = database.server;
            if (isMatch(column.name,target)) {
                filteredServers.add(server);
                filteredDatabases.put(server,database);
                filteredTables.put(database,table);
                filteredColumns.put(table,column);
            }
            if (isMatch(table.name,target)) {
                filteredServers.add(server);
                filteredDatabases.put(server,database);
                filteredTables.put(database,table);
            }
            if (isMatch(database.name,target)) {
                filteredServers.add(server);
                filteredDatabases.put(server,database);
            }
            if (isMatch(server.uri.toString(),target)) {
                filteredServers.add(server);
            }
        }
        return new ServersSearchPage(search,
            new ArrayList(filteredServers),filteredDatabases,
            filteredTables,filteredColumns);
    }

    /**
     * Return a list of all columns from all servers.
     */
    ImmutableList<DBColumn> allColumns() throws SQLException {
        List<DBColumn> columns = Lists.newArrayList();
        ImmutableList<Server> servers = Stores.getServerStores().getServers();
        for (Server server : servers) {
            for (DBColumn column : db.of(server).getColumnsFor(server)) {
                columns.add(column);
            }
        }
        return ImmutableList.copyOf(columns);
    }

    /**
     * Return true if the search should consider this a match.
     */
    static boolean isMatch(String text, String target) {
        return text.toUpperCase().contains(target.toUpperCase());
    }

    @Override
    public boolean handles(String uri) {
        int slashes = URIs.slashCount(uri);
        return slashes == 1 || slashes ==2 && uri.endsWith("/");
    }

    /**
     * Generate a list of all of the databases on all of the servers
     * @param servers
     * @return
     */
    ImmutableMultimap<Server,Object> getDatabases(ImmutableList<Server> servers) {
        Multimap<Server,Object> databases = HashMultimap.create();
        for (Server server : servers) {
            try {
                for (Database database : db.of(server).getDatabasesOn(server)) {
                    databases.put(server, database);
                }
            } catch (Throwable t) {
                databases.put(server, Log.annotatedStackTrace(t));
                log.warn(t);
            }
        }
        return ImmutableMultimap.copyOf(databases);
    }

    static void info(String message) {
        log.info(message);
    }
}

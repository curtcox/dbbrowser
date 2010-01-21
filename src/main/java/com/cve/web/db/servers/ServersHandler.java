package com.cve.web.db.servers;

import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.io.db.DBMetaData;
import com.cve.log.Log;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.Search;
import com.cve.web.db.DBURICodec;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static com.cve.util.Check.notNull;

/**
 * For picking a database server.
 */
final class ServersHandler extends AbstractRequestHandler {

    /**
     * How we access databases.
     */
    final DBMetaData.Factory db;

    final DBServersStore serversStore;

    final DBURICodec codec;

    private final Log log;

    private ServersHandler(DBMetaData.Factory db, DBServersStore serversStore, Log log) {
        super(log);
        this.db = notNull(db);
        this.serversStore = notNull(serversStore);
        this.log = notNull(log);
        codec = DBURICodec.of(log);
    }

    static ServersHandler of(DBMetaData.Factory db, DBServersStore serversStore, Log log) {
        return new ServersHandler(db,serversStore,log);
    }

    @Override
    public Model get(PageRequest request) {
        log.notNullArgs(request);
        Search search = codec.getSearch(request.requestURI);
        if (search.isEmpty()) {
            ImmutableList<DBServer> servers = serversStore.keys();
            ImmutableMultimap<DBServer,Database> databases = getDatabases(servers);
            return ServersPage.of(servers,databases);
        }
        return newSearchPage(search);
    }

    /**
     * Perform the requested search and return a results page.
     */
    ServersSearchPage newSearchPage(Search search) {
        log.notNullArgs(search);
        ImmutableList<DBColumn> columns = allColumns();
        Set<DBServer> filteredServers = Sets.newHashSet();
        Multimap<DBServer,Database> filteredDatabases = HashMultimap.create();
        Multimap<Database,DBTable> filteredTables = HashMultimap.create();
        Multimap<DBTable,DBColumn> filteredColumns = HashMultimap.create();
        String     target = search.target;
        for (DBColumn column : columns) {
            DBTable     table = column.table;
            Database database = table.database;
            DBServer     server = database.server;
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
    ImmutableList<DBColumn> allColumns() {
        List<DBColumn> columns = Lists.newArrayList();
        ImmutableList<DBServer> servers = serversStore.keys();
        for (DBServer server : servers) {
            for (DBColumn column : db.of(server).getColumnsFor(server).value) {
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
    ImmutableMultimap<DBServer,Database> getDatabases(ImmutableList<DBServer> servers) {
        Multimap<DBServer,Database> databases = HashMultimap.create();
        for (DBServer server : servers) {
            for (Database database : db.of(server).getDatabasesOn(server).value) {
                databases.put(server, database);
            }
        }
        return ImmutableMultimap.copyOf(databases);
    }

    void info(String message) {
        log.info(message);
    }
}

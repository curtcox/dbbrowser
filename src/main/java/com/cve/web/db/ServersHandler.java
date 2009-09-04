package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.log.Log;
import com.cve.stores.ServersStore;
import com.cve.util.URIs;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.sql.SQLException;

import static com.cve.log.Log.args;

/**
 * For picking a database server.
 */
public final class ServersHandler extends AbstractRequestHandler {

    private static final Log log = Log.of(ServersHandler.class);

    @Override
    public Model get(PageRequest request) throws IOException, SQLException {
        args(request);
        Search search = DBURICodec.getSearch(request.requestURI);
        if (search.isEmpty()) {
            ImmutableList<Server> servers = ServersStore.getServers();
            ImmutableMultimap<Server,Object> databases = getDatabases(servers);
            return new ServersPage(servers,databases);
        }
        return newSearchPage(search);
    }

    static ServersSearchPage newSearchPage(Search search) {
        ImmutableList<Server> servers = ServersStore.getServers();
        Multimap<Server,Object> columns = HashMultimap.create();
        for (Server server : servers) {
            try {
                for (DBColumn column : DBConnection.getDbmd(server).getColumnsFor(server)) {
                    columns.put(server, column);
                }
            } catch (Throwable t) {
                columns.put(server, Log.annotatedStackTrace(t));
                log.warn(t);
            }
        }
        return new ServersSearchPage(search,servers,ImmutableMultimap.copyOf(columns));
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
    static ImmutableMultimap<Server,Object> getDatabases(ImmutableList<Server> servers) {
        Multimap<Server,Object> databases = HashMultimap.create();
        for (Server server : servers) {
            try {
                for (Database database : DBConnection.getDbmd(server).getDatabasesOn(server)) {
                    databases.put(server, database);
                }
            } catch (Throwable t) {
                databases.put(server, Log.annotatedStackTrace(t));
                log.warn(t);
            }
        }
        return ImmutableMultimap.copyOf(databases);
    }
}

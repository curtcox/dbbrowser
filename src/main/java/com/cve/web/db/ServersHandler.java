package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.log.Log;
import com.cve.stores.ServersStore;
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
    public ServersPage get(PageRequest request) throws IOException, SQLException {
        args(request);
        ImmutableList<Server>              servers = ServersStore.getServers();
        ImmutableMultimap<Server,Object> databases = getDatabases(servers);
        Search search = Search.from(request);
        return new ServersPage(search,servers,databases);
    }

    @Override
    public boolean handles(String uri) {
        return "/".equals(uri);
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

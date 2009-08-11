package com.cve.web.db;

import com.cve.web.*;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.stores.ServersStore;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.sql.SQLException;

/**
 * For picking a database server.
 */
public final class ServersHandler extends AbstractRequestHandler {

    public PageResponse get(PageRequest request) throws IOException, SQLException {
        ImmutableList<Server>                servers = ServersStore.getServers();
        ImmutableMultimap<Server,Database> databases = getDatabases(servers);
        return PageResponse.of(new ServersPage(servers,databases));
    }

    @Override
    public boolean handles(String uri) {
        return "/".equals(uri);
    }

    static ImmutableMultimap<Server,Database> getDatabases(ImmutableList<Server> servers) throws SQLException {
        Multimap<Server,Database> databases = HashMultimap.create();
        for (Server server : servers) {
            for (Database database : DBConnection.getDbmd(server).getDatabasesOn(server)) {
                databases.put(server, database);
            }
        }
        return ImmutableMultimap.copyOf(databases);
    }
}

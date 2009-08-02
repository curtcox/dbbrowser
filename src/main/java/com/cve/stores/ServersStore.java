package com.cve.stores;

import com.cve.db.dbio.DBConnection;
import com.cve.db.ConnectionInfo;
import com.cve.db.SQL;
import com.cve.db.Server;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.Map;

/**
 * The datbase {@link Server}S we know about.
 */
public final class ServersStore {

    /**
     * How to connect to servers.
     */
    private static Map<Server,ConnectionInfo>     INFOS = Maps.newHashMap();

    /**
     * Connections for servers.
     */
    private static Map<Server,DBConnection> CONNECTIONS = Maps.newHashMap();

    /**
     * Add a server and how to connect to it.
     */
    public static void addServer(Server server, ConnectionInfo info) {
        INFOS.put(server, info);
    }

    public static ImmutableList<Server> getServers() {
        return ImmutableList.copyOf(INFOS.keySet());
    }

    public static DBConnection getConnection(Server server) {
        if (CONNECTIONS.containsKey(server)) {
            return CONNECTIONS.get(server);
        }
        try {
            DBConnection connection = tryGetConnection(server);
            CONNECTIONS.put(server, connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DBConnection tryGetConnection(Server server) throws SQLException {
        final ConnectionInfo info = INFOS.get(server);
        if (info==null) {
            String message = "No connection info for " + server +
                ".  Connection available for " + CONNECTIONS.keySet();
            throw new IllegalArgumentException(message);
        }
        return DBConnection.info(info);
    }

}

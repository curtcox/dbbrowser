package com.cve.stores;

import com.cve.db.dbio.DBConnection;
import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.util.Strings;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
     * The name of the file we persist to.
     */
    private static String SERVERS = "servers";

    private static String LIST_OF_ALL_SERVERS = "ListOfAllServers";
    private static String USER = "user";
    private static String PASSWORD = "password";
    private static String URL = "jdbcurl";

    /**
     * When the VM starts, load the servers.
     */
    static {
        load();
    }

    private static void load() {
        Properties props = PropertiesIO.load(SERVERS);
        String all = props.getProperty(LIST_OF_ALL_SERVERS);
        if (all==null || all.equals("")) {
            return;
        }
        for (String name : all.split(",")) {
            loadServer(props,name);
        }
    }

    /**
     * Load the named server.
     */
    private static void loadServer(Properties props,String name) {
        Server   server = Server.uri(URIs.of(name));
        JDBCURL jdbcurl = JDBCURL.uri(URIs.of(props.getProperty(name + "." + URL)));
        String     user = props.getProperty(name + "." + USER);
        String password = props.getProperty(name + "." + PASSWORD);
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcurl, user, password);
        addServer(server,info);
    }

    /**
     * Save the named server.
     */
    private static void saveServer(Properties props, Server server) {
        String name = server.uri.toString();
        ConnectionInfo info = INFOS.get(server);
        props.setProperty(name + "." + URL,      info.url.toString());
        props.setProperty(name + "." + USER,     info.user);
        props.setProperty(name + "." + PASSWORD, info.password);
    }

    private static void save() {
        Properties props = new Properties();
        for (Server server : getServers()) {
           saveServer(props,server);
        }
        props.setProperty(LIST_OF_ALL_SERVERS, getServerList());
        PropertiesIO.save(props,SERVERS);
    }

    /**
     * Get a comma separated list of all our server names.
     */
    private static String getServerList() {
        List<String> list = Lists.newArrayList();
        for (Server server : getServers()) {
            list.add(server.toString());
        }
        return Strings.separated(list, ",");
    }

    /**
     * Add a server and how to connect to it.
     */
    public static void addServer(Server server, ConnectionInfo info) {
        INFOS.put(server, info);
        save();
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

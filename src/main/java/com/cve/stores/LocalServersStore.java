package com.cve.stores;

import com.cve.db.dbio.DBConnection;
import com.cve.db.ConnectionInfo;
import com.cve.db.Database;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnectionFactory;
import static com.cve.util.Check.notNull;
import com.cve.util.Strings;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import static com.cve.log.Log.args;

/**
 * The datbase {@link Server}S we know about.
 */
final class LocalServersStore implements ServersStore {

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
     * Has the store been been initially loaded?
     * Is startup over?
     */
    private static boolean loaded = false;

    /**
     * Load the list of servers we know about from disk.
     * While it would be nice to be able to do this automatically in a static
     * initializer, doing so makes it harder to write a complete set of
     * unit tests.
     */
    @Override
    public void load() {
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
    private void loadServer(Properties props,String name) {
        try {
            Server   server = Server.uri(URIs.of(name));
            JDBCURL jdbcurl = JDBCURL.uri(URIs.of(notNull(props.getProperty(name + "." + URL),URL)));
            String     user = notNull(props.getProperty(name + "." + USER) , USER);
            String password = notNull(props.getProperty(name + "." + PASSWORD) , PASSWORD);
            ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcurl, user, password);
            addServer(server,info);
        } catch (RuntimeException e) {
            String message = "Loading " + name;
            throw new RuntimeException(message,e);
        }
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

    private void save() {
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
    private String getServerList() {
        List<String> list = Lists.newArrayList();
        for (Server server : getServers()) {
            list.add(server.toString());
        }
        return Strings.separated(list, ",");
    }

    /**
     * Add a server and how to connect to it.
     */
    @Override
    public void addServer(Server server, ConnectionInfo info) {
        INFOS.put(server, info);
        // Only save the server if the server list has been loaded.
        // It won't have been loaded if:
        // a) it is still loading
        // b) we are running unit tests
        if (loaded) {
            save();
        }
    }

    /**
     * Get a list of all servers in the store.
     */
    @Override
    public ImmutableList<Server> getServers() {
        return ImmutableList.copyOf(INFOS.keySet());
    }

    /**
     * Get a connection to the given server.
     */
    @Override
    public DBConnection getConnection(Server server) {
        args(server);
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

    /**
     * Get a connection to the given server and database.
     */
    @Override
    public DBConnection getConnection(Server server, Database database) {
        throw new UnsupportedOperationException();
    }

    private static DBConnection tryGetConnection(Server server) throws SQLException {
        final ConnectionInfo info = INFOS.get(server);
        if (info==null) {
            String message = "No connection info for " + server +
                ".  Connection available for " + CONNECTIONS.keySet();
            throw new IllegalArgumentException(message);
        }
        return DBConnectionFactory.of(info);
    }

}

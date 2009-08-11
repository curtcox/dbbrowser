package com.cve.web.db;

import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.URIs;
import com.cve.web.*;

/**
 * For adding a server.
 */
public final class AddServerPage implements Model {

    /**
     * To the user
     */
    final String message;
    final Server server;
    final ConnectionInfo info;

    /**
     * Literal to use in the form.
     */
    static final String USER     = "user";
    static final String PASSWORD = "password";
    static final String URL      = "jdbcurl";

    static final AddServerPage SAMPLE = new AddServerPage(
       "Specify the server you want to connect to",
        Server.NULL,
        ConnectionInfo.urlUserPassword(
            JDBCURL.uri(URIs.of(URL)), USER, PASSWORD)
        );


    private AddServerPage(String message, Server server, ConnectionInfo info) {
        this.message = Check.notNull(message);
        this.server  = Check.notNull(server);
        this.info    = Check.notNull(info);
    }

    static AddServerPage messageServerInfo(String message, Server server, ConnectionInfo info)
    {
        return new AddServerPage(message,server,info);
    }
}

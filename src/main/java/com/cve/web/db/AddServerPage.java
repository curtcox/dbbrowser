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
    final String user;
    final String password;
    final ConnectionInfo info;

    /**
     * Literals to use in the form.
     */
    static final String SERVER   = "server";
    static final String USER     = "user";
    static final String PASSWORD = "password";
    static final String URL      = "jdbcurl";

    static final AddServerPage SAMPLE = new AddServerPage(
       "Specify the server you want to connect to",
        Server.NULL, "user", "password",
        ConnectionInfo.NULL
    );


    private AddServerPage(String message, Server server, String user, String password, ConnectionInfo info) {
        this.message  = Check.notNull(message);
        this.server   = Check.notNull(server);
        this.user     = Check.notNull(user);
        this.password = Check.notNull(password);
        this.info     = Check.notNull(info);
    }

    static AddServerPage messageServerInfo(String message, Server server, ConnectionInfo info)
    {
        return new AddServerPage(message,server,info.getUser(),info.getPassword(),info);
    }
}

package com.cve.web.db;

import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.web.*;
import javax.annotation.concurrent.Immutable;

/**
 * For adding a server.
 */
@Immutable
public final class AddServerPage implements Model {

    /**
     * To the user
     */
    final String message;
    final Server server;
    final String jdbcurl;
    final String user;
    final String password;

    /**
     * Literals to use in the form.
     */
    static final String SERVER   = "server";
    static final String USER     = "user";
    static final String PASSWORD = "password";
    static final String URL      = "jdbcurl";

    static final AddServerPage SAMPLE = new AddServerPage(
       "Specify the server you want to connect to",
        Server.NULL, "user", "password", "jdbcurl"
    );


    private AddServerPage(String message, Server server, String user, String password, String jdbcurl) {
        this.message  = Check.notNull(message);
        this.server   = Check.notNull(server);
        this.user     = Check.notNull(user);
        this.password = Check.notNull(password);
        this.jdbcurl  = Check.notNull(jdbcurl);
    }

    static AddServerPage messageServerUserPasswordJdbcUrl(
        String message, Server server, String user, String password, String jdbcurl)
    {
        return new AddServerPage(message,server,user,password,jdbcurl);
    }
}

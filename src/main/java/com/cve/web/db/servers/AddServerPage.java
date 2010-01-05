package com.cve.web.db.servers;

import com.cve.db.DBServer;
import com.cve.util.Check;
import com.cve.web.*;
import javax.annotation.concurrent.Immutable;

/**
 * For adding a server.
 */
@Immutable
final class AddServerPage implements Model {

    /**
     * To the user
     */
    final String message;
    final DBServer server;
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
        DBServer.NULL, "user", "password", "jdbcurl"
    );


    private AddServerPage(String message, DBServer server, String user, String password, String jdbcurl) {
        this.message  = Check.notNull(message);
        this.server   = Check.notNull(server);
        this.user     = Check.notNull(user);
        this.password = Check.notNull(password);
        this.jdbcurl  = Check.notNull(jdbcurl);
    }

    static AddServerPage messageServerUserPasswordJdbcUrl(
        String message, DBServer server, String user, String password, String jdbcurl)
    {
        return new AddServerPage(message,server,user,password,jdbcurl);
    }
}

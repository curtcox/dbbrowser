package com.cve.db;

import com.cve.db.dbio.DBDriver;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Information about how to connect to a database.
 * In other words -- URL, user, and pasword.
 * @author curt
 */
@Immutable
public final class ConnectionInfo {

    private final JDBCURL         url;
    private final String         user;
    private final String     password;
    private final DBDriver     driver;

    public static final ConnectionInfo NULL = new ConnectionInfo();

    private ConnectionInfo() {
        url      = null;
        user     = null;
        password = null;
        driver   = null;
    }

    /**
     * Use the factory.
     */
    private ConnectionInfo(JDBCURL url, String user, String password) {
        this.url      = notNull(url);
        this.user     = notNull(user);
        this.password = notNull(password);
        driver        = DBDriver.url(url);
    }

    /**
     * Factory for creating ConnectionInfoS.
     */
    public static ConnectionInfo urlUserPassword(JDBCURL url, String user, String password) {
        return new ConnectionInfo(url,user,password);
    }

    public JDBCURL       getURL() { return url; }
    public String       getUser() { return user; }
    public String   getPassword() { return password; }
    public DBDriver   getDriver() { return driver; }

}

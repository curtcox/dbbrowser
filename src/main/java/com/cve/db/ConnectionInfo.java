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

    public final JDBCURL         url;
    public final String         user;
    public final String     password;
    public final DBDriver     driver;

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

    @Override
    public String toString() {
        return " url=" + url +
               " user=" + user +
               " password=" + password +
               " driver=" + driver;
    }
}

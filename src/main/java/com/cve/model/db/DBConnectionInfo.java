package com.cve.model.db;

import com.cve.io.db.driver.DBDriver;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Information about how to connect to a database.
 * In other words -- URL, user, and pasword.
 * @author curt
 */
@Immutable
public final class DBConnectionInfo {

    public final JDBCURL         url;
    public final String         user;
    public final String     password;
    public final DBDriver     driver;

    public static final DBConnectionInfo NULL = new DBConnectionInfo();

    private DBConnectionInfo() {
        url      = null;
        user     = null;
        password = null;
        driver   = null;
    }

    /**
     * Use the factory.
     */
    private DBConnectionInfo(JDBCURL url, String user, String password, DBDriver driver) {
        this.url      = notNull(url);
        this.user     = notNull(user);
        this.password = notNull(password);
        this.driver   = notNull(driver);
    }

    /**
     * Factory for creating ConnectionInfoS.
     */
    public static DBConnectionInfo urlUserPassword(JDBCURL url, String user, String password, DBDriver driver) {
        return new DBConnectionInfo(url,user,password,driver);
    }

    @Override
    public String toString() {
        return " url=" + url +
               " user=" + user +
               " password=" + password +
               " driver=" + driver;
    }
}

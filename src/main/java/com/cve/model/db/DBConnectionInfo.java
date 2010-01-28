package com.cve.model.db;

import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.log.Logs;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Information about how to connect to a database.
 * In other words -- URL, user, and pasword.
 * @author curt
 */
@Immutable
public final class DBConnectionInfo {

    public final Log             log;
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
        log      = Logs.of();
    }

    /**
     * Use the factory.
     */
    private DBConnectionInfo(JDBCURL url, String user, String password, DBDriver driver, Log log) {
        this.url      = notNull(url);
        this.user     = notNull(user);
        this.password = notNull(password);
        this.driver   = notNull(driver);
        this.log      = notNull(log);
    }

    /**
     * Factory for creating ConnectionInfoS.
     */
    public static DBConnectionInfo urlUserPassword(JDBCURL url, String user, String password, DBDriver driver, Log log) {
        return new DBConnectionInfo(url,user,password,driver,log);
    }

    @Override
    public String toString() {
        return " url=" + url +
               " user=" + user +
               " password=" + password +
               " driver=" + driver;
    }
}

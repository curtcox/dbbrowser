package com.cve.io.db.driver;

import com.cve.io.db.SelectRenderer;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.DBMetaDataIO;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.io.db.driver.derby.DerbyDriver;
import com.cve.io.db.driver.h2.H2Driver;
import com.cve.io.db.driver.mssql.MsSQLDriver;
import com.cve.io.db.driver.mysql.MySQLDriver;
import com.cve.io.db.driver.oracle.OracleDriver;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import java.sql.Driver;
import com.cve.util.Check;
import com.cve.web.Search;

/**
 * Database drivers that we support.
 * @author curt
 */
public enum DBDriver {

    MySql("jdbc:mysql:", com.mysql.jdbc.Driver.class, MySQLDriver.of()),
    MsSqlTds("jdbc:jtds:sqlserver:", net.sourceforge.jtds.jdbc.Driver.class, MsSQLDriver.of()),
    H2("jdbc:h2:",    org.h2.Driver.class, H2Driver.of()),
    Derby("jdbc:derby:", org.apache.derby.jdbc.ClientDriver.class,DerbyDriver.of()),
    Oracle("jdbc:oracle:thin:", org.h2.Driver.class,OracleDriver.of()),
    ;

    /**
     * We handle JDBC URLs that start with this.
     */
    private final String prefix;

    private final DriverIO io;

    /**
     * The JDBC driver class we wrap
     */
    private final Class<? extends Driver> driver;

    DBDriver(String prefix,Class<? extends Driver> driver, DriverIO io) {
        this.prefix = Check.notNull(prefix);
        this.driver = Check.notNull(driver);
        this.io     = Check.notNull(io);
    }

    /**
     * Return the proper JDBC URL for this driver, when the server is on
     * the given machine name.
     */
    public final JDBCURL getJDBCURL(String name) {
        return io.getJDBCURL(name);
    }

    /**
     * Return the mechanism for getting metadta from databases using this driver.
     */
    public final DBMetaData getDBMetaData(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return io.getDBMetaData(dbmd,managedFunction, serversStore);
    }

    /**
     * Return how to turn Select objects to SQL.
     * @return
     */
    final SelectRenderer getSelectRenderer() {
        return io.getSelectRenderer();
    }

    final DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return io.getResultSetFactory(server, meta);
    }

    /**
     * Return the SQL appropriate for this select.
     */
    final public SQL render(Select select, Search search) {
        return getSelectRenderer().render(select,search);
    }

    /**
     * Return the SQL appropriate to count the rows in this select.
     */
    final public SQL renderCount(Select select, Search search) {
        return getSelectRenderer().renderCount(select,search);
    }

    /**
     * Return the proper connection info for this driver, given server machine
     * name, user name and password.
     */
    public DBConnectionInfo getConnectionInfo(String name, String user, String password) {
        JDBCURL     jdbcURL = getJDBCURL(name);
        DBConnectionInfo info = DBConnectionInfo.urlUserPassword(jdbcURL, user, password);
        return         info;
    }

    /**
     * Load the right driver for the given URL.
     */
    public static DBDriver url(JDBCURL url) {
        String string = url.toString();
        for (DBDriver driver : values()) {
            if (string.startsWith(driver.prefix)) {
                return driver;
            }
        }
        throw new IllegalArgumentException("No driver found for " + url);
    }


    /**
     * Force all of our drivers to load on startup.
     * Right now, only the TDS driver needs to be forced like this, but
     * this is the standard way to load a JDBC driver, so this is a good
     * thing to do.
     */
    static {
        for (DBDriver driver : values()) {
            try {
                Class.forName(driver.driver.getCanonicalName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.JDBCURL;
import com.cve.util.URIs;
import java.sql.Driver;
import com.cve.util.Check;

/**
 * Database drivers that we support.
 * @author curt
 */
public enum DBDriver {


    MySql("jdbc:mysql:", com.mysql.jdbc.Driver.class) {
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:mysql://" + name + ":3306";
        return JDBCURL.uri(URIs.of(url));
    }} ,

    MsSqlTds("jdbc:jtds:sqlserver:", net.sourceforge.jtds.jdbc.Driver.class) {
    @Override
    public JDBCURL getJDBCURL(String name) {
    /**
     * Return a connection for this info.
     * The URL format for jTDS is:

        jdbc:jtds:<server_type>://<server>[:<port>][/<database>][;<property>=<value>[;...]]

        where <server_type> is one of either 'sqlserver' or 'sybase' (their meaning is quite obvious),
        <port> is the port the database server is listening to (default is 1433 for SQL Server and 7100 for Sybase)
         and <database> is the database name -- JDBC term:
         catalog -- (if not specified, the user's default database is used).
          The set of properties supported by jTDS is:
     */

        //Define URL of database server with the default port number 1433.
        String url = "jdbc:jtds:sqlserver://" + name + ":1433";
        return JDBCURL.uri(URIs.of(url));
    }} ,


//    MsSql("jdbc:sqlserver:", com.microsoft.sqlserver.jdbc.SQLServerDriver.class) {
//    JDBCURL getJDBCURL(String name, String user, String password) {
    /**
    When the driver is loaded, you can establish a connection by using a connection URL:

    String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
       "databaseName=AdventureWorks;user=MyUserName;password=*****;";
    */

        //Define URL of database server with the default port number 1433.
//        String url = "jdbc:sqlserver://" + name + ":1433;user=" +  user +";password=" +  password;
//        return JDBCURL.uri(URIs.of(url));
//    }} ,

    H2("jdbc:h2:",    org.h2.Driver.class) {
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:h2:" + name;
        return JDBCURL.uri(URIs.of(url));
    }} ,
    
    Derby("jdbc:derby:", org.apache.derby.jdbc.ClientDriver.class) {
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:derby:" + name;
        return JDBCURL.uri(URIs.of(url));
    }} ,

    Oracle("jdbc:oracle:thin:", org.h2.Driver.class) {
    @Override
    public JDBCURL getJDBCURL(String name) {
        String url = "jdbc:oracle:thin:" + name;
        return JDBCURL.uri(URIs.of(url));
    }} ,
    ;

    private final String prefix;
    private final Class<? extends Driver> driver;

    DBDriver(String prefix,Class<? extends Driver> driver) {
        this.prefix = Check.notNull(prefix);
        this.driver = Check.notNull(driver);
    }

    /**
     * Return the proper JDBC URL for this driver, when the server is on
     * the given machine name.
     */
    public abstract JDBCURL getJDBCURL(String name);

    /**
     * Return the proper connection info for this driver, given server machine
     * name, user name and password.
     */
    public ConnectionInfo getConnectionInfo(String name, String user, String password) {
        JDBCURL     jdbcURL = getJDBCURL(name);
        ConnectionInfo info = ConnectionInfo.urlUserPassword(jdbcURL, user, password);
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

package com.cve.io.db.driver.mssql;

import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DriverIO;
import com.cve.stores.ManagedFunction;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;

/**
 *
 * @author curt
 */
public final class MsSQLDriver implements DriverIO {

    private MsSQLDriver() {}

    public static DriverIO of() {
        return new MsSQLDriver();
    }

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
    @Override
    public JDBCURL getJDBCURL(String name) {
        //Define URL of database server with the default port number 1433.
        String url = "jdbc:jtds:sqlserver://" + name + ":1433";
        return JDBCURL.uri(URIs.of(url));
    }

    @Override
    public DBMetaData getDBMetaData(DBMetaDataIO dbmd, ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return MsSQLTdsMetaData.of(dbmd,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return MsSqlSelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new MsSqlTdsResultSetMetaDataFactory(server, meta);
    }
}

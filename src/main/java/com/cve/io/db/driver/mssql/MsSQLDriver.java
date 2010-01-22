package com.cve.io.db.driver.mssql;

import com.cve.io.db.DBConnection;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DriverIO;
import com.cve.log.Log;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
public final class MsSQLDriver implements DriverIO {

    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;
    final Log log;
    
    private MsSQLDriver(ManagedFunction.Factory managedFunction, DBServersStore serversStore, Log log) {
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        this.log = notNull(log);
    }

    public static DriverIO of(ManagedFunction.Factory managedFunction, DBServersStore serversStore, Log log) {
        return new MsSQLDriver(managedFunction, serversStore,log);
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
    public DBMetaData getDBMetaData(DBConnection connection) {
        return MsSQLTdsMetaData.of(connection,managedFunction,serversStore,log);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return MsSqlSelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new MsSqlTdsResultSetMetaDataFactory(server, meta,log);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        return MsSQLTdsMetaDataIO.of(connection, managedFunction,log);
    }
}

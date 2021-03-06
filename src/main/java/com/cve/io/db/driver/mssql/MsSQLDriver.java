package com.cve.io.db.driver.mssql;

import com.cve.io.db.DBConnection;
import com.cve.model.db.DBServer;
import com.cve.model.db.JDBCURL;
import com.cve.io.db.DBMetaData;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.stores.ManagedFunction;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.SelectRenderer;
import com.cve.io.db.driver.DefaultDBResultSetMetaDataFactory;
import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.db.DBServersStore;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;
import com.cve.web.core.Search;

/**
 *
 * @author curt
 */
public final class MsSQLDriver implements DBDriver {

    final ManagedFunction.Factory managedFunction;
    final DBServersStore serversStore;
    final Log log = Logs.of();
    
    private MsSQLDriver(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        this.managedFunction = notNull(managedFunction);
        this.serversStore = notNull(serversStore);
        
    }

    public static DBDriver of(ManagedFunction.Factory managedFunction, DBServersStore serversStore) {
        return new MsSQLDriver(managedFunction, serversStore);
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
        return MsSQLTdsMetaData.of(connection,managedFunction,serversStore);
    }

    @Override
    public SelectRenderer getSelectRenderer() {
        return MsSqlSelectRenderer.of();
    }

    @Override
    public DefaultDBResultSetMetaDataFactory getResultSetFactory(DBServer server, DBResultSetMetaDataIO meta) {
        return new MsSqlTdsResultSetMetaDataFactory(server, meta);
    }

    @Override
    public DBMetaDataIO getDBMetaDataIO(DBConnection connection) {
        return MsSQLTdsMetaDataIO.of(connection, managedFunction);
    }

    @Override
    public SQL render(Select select, Search search) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SQL renderCount(Select select, Search search) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean handles(JDBCURL url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

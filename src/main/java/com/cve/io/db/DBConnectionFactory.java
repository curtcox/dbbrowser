package com.cve.io.db;

import com.cve.io.db.driver.DBDrivers;
import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;
import java.sql.SQLException;
import static com.cve.util.Check.notNull;

/**
 * For confining implementations to this package.
 * @author curt
 */
public final class DBConnectionFactory {

    final DBServersStore serversStore;

    final ManagedFunction.Factory managedFunction;

    final Log log = Logs.of();

    private DBConnectionFactory(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        this.serversStore = notNull(serversStore);
        this.managedFunction = notNull(managedFunction);
        
    }

    public static DBConnectionFactory of(DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return new DBConnectionFactory(serversStore,managedFunction);
    }

    public DBConnection getConnection(DBConnectionInfo info) {
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public DBConnection getConnection(DBServer server) {
        log.args(server,serversStore,managedFunction);
        DBConnectionInfo info = serversStore.get(server);
        if (info==null) {
            String message = server + " not found in store";
            throw new IllegalArgumentException(message);
        }
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public DBMetaDataIO getDbmdIO(DBServer server,DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        log.args(server,serversStore,managedFunction);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server);
        DBDriver driver = DBDrivers.of(managedFunction,serversStore).url(connection.info.url);
        DBMetaDataIO io = driver.getDBMetaDataIO(connection);
        return io;
    }
    
    public java.sql.DatabaseMetaData metaFor(DBServer server,DBServersStore serversStore,ManagedFunction.Factory managedFunction) throws SQLException {
        log.args(server,serversStore,managedFunction);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server);
        return connection.getJDBCMetaData();
    }

    public static DBConnection getConnection(DBServer server, Database database, DBServersStore serversStore, Factory managedFunction) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public DBConnection getConnection(DBServer server, Database database) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

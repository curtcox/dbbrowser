package com.cve.io.db;

import com.cve.io.db.driver.DBDriver;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;
import java.sql.SQLException;
import static com.cve.log.Log.notNullArgs;
/**
 * For confining implementations to this package.
 * @author curt
 */
public final class DBConnectionFactory {

    public static DBConnection getConnection(DBConnectionInfo info, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public static DBConnection getConnection(DBServer server, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        notNullArgs(server,serversStore,managedFunction);
        DBConnectionInfo info = serversStore.get(server);
        if (info==null) {
            String message = server + " not found in store";
            throw new IllegalArgumentException(message);
        }
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public static DBMetaDataIO getDbmdIO(DBServer server,DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        notNullArgs(server,serversStore,managedFunction);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server,serversStore,managedFunction);
        DBDriver driver = DBDriver.url(connection.info.url);
        DBMetaDataIO io = driver.getDBMetaDataIO(connection,managedFunction);
        return io;
    }
    
    public static java.sql.DatabaseMetaData metaFor(DBServer server,DBServersStore serversStore,ManagedFunction.Factory managedFunction) throws SQLException {
        notNullArgs(server,serversStore,managedFunction);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server,serversStore,managedFunction);
        return connection.getJDBCMetaData();
    }

    public static DBConnection getConnection(DBServer server, Database database, DBServersStore serversStore, Factory managedFunction) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

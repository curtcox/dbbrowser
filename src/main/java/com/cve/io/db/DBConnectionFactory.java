package com.cve.io.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.DBServersStore;
import java.sql.SQLException;
import static com.cve.log.Log.args;
import static com.cve.util.Check.notNull;
/**
 * For confining implementations to this package.
 * @author curt
 */
public final class DBConnectionFactory {

    public static DBConnection getConnection(DBConnectionInfo info, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public static DBConnection getConnection(DBServer server, DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        args(server,serversStore,managedFunction);
        notNull(server);
        notNull(serversStore);
        notNull(managedFunction);
        DBConnectionInfo info = serversStore.get(server);
        if (info==null) {
            String message = server + " not found in store";
            throw new IllegalArgumentException(message);
        }
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public static DBMetaDataIO getDbmdIO(DBServer server,DBServersStore serversStore, ManagedFunction.Factory managedFunction) {
        args(server,serversStore,managedFunction);
        notNull(server);
        notNull(serversStore);
        notNull(managedFunction);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server,serversStore,managedFunction);
        DBMetaDataIO   dbmd = DefaultDBMetaDataIO.of(connection,managedFunction);
        return dbmd;
    }
    
    public static java.sql.DatabaseMetaData metaFor(DBServer server,DBServersStore serversStore,ManagedFunction.Factory managedFunction) throws SQLException {
        args(server,serversStore,managedFunction);
        notNull(server);
        notNull(serversStore);
        notNull(managedFunction);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server,serversStore,managedFunction);
        return connection.getJDBCMetaData();
    }

    public static DBConnection getConnection(DBServer server, Database database, DBServersStore serversStore, Factory managedFunction) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

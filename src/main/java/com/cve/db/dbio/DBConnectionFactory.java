package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ServersStore;
import com.cve.stores.Stores;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * For confining implementations to this package.
 * @author curt
 */
public final class DBConnectionFactory {

    public static DBConnection of(ConnectionInfo info, ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return DefaultDBConnection.info(info,serversStore,managedFunction);
    }

    private static DefaultDBConnection getConnection(Server server,ManagedFunction.Factory managedFunction) {
        return (DefaultDBConnection) Stores.getServerStore(managedFunction).getConnection(server);
    }

    public static DBMetaDataIO getDbmdIO(Server server,ManagedFunction.Factory managedFunction) {
        args(server);
        DefaultDBConnection connection = getConnection(server,managedFunction);
        DBMetaDataIO   dbmd = DefaultDBMetaDataIO.connection(connection,managedFunction);
        return dbmd;
    }
    
    public static java.sql.DatabaseMetaData metaFor(Server server,ManagedFunction.Factory managedFunction) throws SQLException {
        DefaultDBConnection connection = getConnection(server,managedFunction);
        return connection.getJDBCMetaData();
    }
}

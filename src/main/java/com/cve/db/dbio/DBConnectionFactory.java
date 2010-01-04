package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.stores.ManagedFunction;
import com.cve.stores.ManagedFunction.Factory;
import com.cve.stores.db.ServersStore;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * For confining implementations to this package.
 * @author curt
 */
public final class DBConnectionFactory {

    public static DBConnection getConnection(ConnectionInfo info, ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public static DBConnection getConnection(Server server, ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        ConnectionInfo info = null;
        return DefaultDBConnection.of(info,serversStore,managedFunction);
    }

    public static DBMetaDataIO getDbmdIO(Server server,ServersStore serversStore, ManagedFunction.Factory managedFunction) {
        args(server);
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server,serversStore,managedFunction);
        DBMetaDataIO   dbmd = DefaultDBMetaDataIO.connection(connection,managedFunction);
        return dbmd;
    }
    
    public static java.sql.DatabaseMetaData metaFor(Server server,ServersStore serversStore,ManagedFunction.Factory managedFunction) throws SQLException {
        DefaultDBConnection connection = (DefaultDBConnection) getConnection(server,serversStore,managedFunction);
        return connection.getJDBCMetaData();
    }

    public static DBConnection getConnection(Server server, Database database, ServersStore serversStore, Factory managedFunction) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

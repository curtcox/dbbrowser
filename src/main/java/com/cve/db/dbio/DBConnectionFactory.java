package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.Server;
import com.cve.stores.ServersStore;
import java.sql.SQLException;
import static com.cve.log.Log.args;

/**
 * For confining implementations to this package.
 * @author curt
 */
public final class DBConnectionFactory {

    public static DBConnection of(ConnectionInfo info) {
        return DefaultDBConnection.info(info);
    }

    public static DBMetaDataIO getDbmdIO(Server server) {
        args(server);
        DefaultDBConnection connection = (DefaultDBConnection) ServersStore.getConnection(server);
        DBMetaDataIO   dbmd = DefaultDBMetaDataIO.connection(connection);
        return dbmd;
    }
    
    public static java.sql.DatabaseMetaData metaFor(Server server) throws SQLException {
        DefaultDBConnection connection = (DefaultDBConnection) ServersStore.getConnection(server);
        return connection.getJDBCMetaData();
    }
}

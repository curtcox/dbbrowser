package com.cve.db.dbio;

import com.cve.db.ConnectionInfo;
import com.cve.db.Server;
import com.cve.stores.Stores;
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

    private static DefaultDBConnection getConnection(Server server) {
        return (DefaultDBConnection) Stores.getServerStore().getConnection(server);
    }

    public static DBMetaDataIO getDbmdIO(Server server) {
        args(server);
        DefaultDBConnection connection = getConnection(server);
        DBMetaDataIO   dbmd = DefaultDBMetaDataIO.connection(connection);
        return dbmd;
    }
    
    public static java.sql.DatabaseMetaData metaFor(Server server) throws SQLException {
        DefaultDBConnection connection = getConnection(server);
        return connection.getJDBCMetaData();
    }
}

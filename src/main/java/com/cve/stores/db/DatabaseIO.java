package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
import static com.cve.util.Check.notNull;
import com.cve.util.URIs;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class DatabaseIO implements IO<Database> {

    final Log log = Logs.of();

    final IO<String> stringIO = StringIO.of();

    private DatabaseIO() {
        
    }

    static DatabaseIO of() {
        return new DatabaseIO();
    }

    @Override
    public byte[] write(Database value) {
        return stringIO.write(value.toString());
    }

    @Override
    public Database read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[] parts = notNull(line).split(".");
        DBServer server = DBServer.uri(URIs.of(parts[0]));
        return server.databaseName(parts[1]);
    }

}

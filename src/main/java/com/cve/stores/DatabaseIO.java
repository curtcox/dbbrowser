package com.cve.stores;

import com.cve.db.Database;
import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.URIs;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class DatabaseIO implements IO<Database> {

    final IO<String> stringIO = StringIO.of();

    final static DatabaseIO SINGLETON = new DatabaseIO();

    private DatabaseIO() {}

    static DatabaseIO of() {
        return SINGLETON;
    }

    @Override
    public byte[] write(Database value) {
        return stringIO.write(value.toString());
    }

    @Override
    public Database read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[] parts = Check.notNull(line).split(".");
        Server server = Server.uri(URIs.of(parts[0]));
        return server.databaseName(parts[1]);
    }

}

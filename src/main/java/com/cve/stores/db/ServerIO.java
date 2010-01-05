package com.cve.stores.db;

import com.cve.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
import com.cve.util.Check;
import com.cve.util.URIs;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class ServerIO implements IO<DBServer> {

    final IO<String> stringIO = StringIO.of();

    final static ServerIO SINGLETON = new ServerIO();

    private ServerIO() {}

    static ServerIO of() {
        return SINGLETON;
    }
    
    @Override
    public DBServer read(byte[] bytes) {
        return DBServer.uri(URIs.of(Check.notNull(stringIO.read(bytes))));
    }

    @Override
    public byte[] write(DBServer value) {
        return stringIO.write(value.toString());
    }

}

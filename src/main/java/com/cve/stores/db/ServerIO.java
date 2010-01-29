package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
import static com.cve.util.Check.notNull;
import com.cve.util.URIs;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class ServerIO implements IO<DBServer> {

    final Log log = Logs.of();

    final IO<String> stringIO = StringIO.of();

    private ServerIO() {
        
    }

    static ServerIO of() {
        return new ServerIO();
    }
    
    @Override
    public DBServer read(byte[] bytes) {
        return DBServer.uri(URIs.of(notNull(stringIO.read(bytes))));
    }

    @Override
    public byte[] write(DBServer value) {
        return stringIO.write(value.toString());
    }

}

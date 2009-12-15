package com.cve.stores;

import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.URIs;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class ServerIO implements IO<Server> {

    final IO<String> stringIO = StringIO.of();

    final static ServerIO SINGLETON = new ServerIO();

    private ServerIO() {}

    static ServerIO of() {
        return SINGLETON;
    }
    
    @Override
    public Server read(byte[] bytes) {
        return Server.uri(URIs.of(Check.notNull(stringIO.read(bytes))));
    }

    @Override
    public byte[] write(Server value) {
        return stringIO.write(value.toString());
    }

}

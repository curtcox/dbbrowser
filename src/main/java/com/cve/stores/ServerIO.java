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
    public Server parse(byte[] bytes) {
        return Server.uri(URIs.of(Check.notNull(stringIO.parse(bytes))));
    }

    @Override
    public byte[] format(Server value) {
        return stringIO.format(value.toString());
    }

}

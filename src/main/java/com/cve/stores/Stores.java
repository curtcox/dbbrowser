package com.cve.stores;

/**
 *
 */
public final class Stores {

    private final static ServersStore SERVERS = new LocalServersStore();

    public static ServersStore getServerStores() {
        return SERVERS;
    }
}

package com.cve.io.fs;

import com.cve.model.fs.FSServer;
import com.cve.io.fs.FSMetaData.Factory;
import com.cve.stores.ManagedFunction;
import com.cve.stores.fs.FSServersStore;

/**
 *
 * @author curt
 */
public final class LocalFSMetaDataFactory implements FSMetaData.Factory {

    /**
     * Use the factory
     */
    private LocalFSMetaDataFactory() {}

    public static Factory of(FSServersStore fsServersStore, ManagedFunction.Factory managedFunction) {
        return new LocalFSMetaDataFactory();
    }

    @Override
    public FSMetaData of(FSServer server) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

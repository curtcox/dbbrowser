package com.cve.stores;

import com.cve.stores.Store.Factory;

/**
 *
 * @author Curt
 */
public final class LocalStoreFactory implements Store.Factory {

    public static Factory of() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public <K, V> Store<K, V> of(Class k, Class v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T of(Class<T> t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

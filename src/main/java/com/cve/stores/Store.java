package com.cve.stores;

import com.google.common.collect.ImmutableList;

/**
 * This is both how we persist things.
 * It is also a key interfaces for distributed mirroring, which is handled
 * by the appropriate Store.Factory.
 * @author curt
 */
public interface Store <K,V> {

    /**
     * For creating StoreS.
     */
    interface Factory {
        <T> T of(Class<T> t);
        <K,V> Store<K,V> of(Class k, Class v);
    }

    /**
     * Get the value for this key.
     */
    V get(K key);

    /**
     * Set the value for this key.
     */
    void put(K key, V value);

    /**
     * Return all of the keys in this store.
     */
    ImmutableList<K> keys();
}

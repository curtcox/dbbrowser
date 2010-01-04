package com.cve.stores;

import com.google.common.collect.ImmutableList;

/**
 *
 * @author curt
 */
public interface Store <K,V> {

    V get(K key);

    void put(K key, V value);

    ImmutableList<K> keySet();
}

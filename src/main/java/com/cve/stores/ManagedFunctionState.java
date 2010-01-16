package com.cve.stores;

import com.cve.util.Timestamp;
import com.google.common.collect.ImmutableMap;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author curt
 */
@Immutable
public final class ManagedFunctionState<K,V> {

    final Timestamp timestamp;

    final ImmutableMap<K,V> values;

    final ImmutableMap<K,Timestamp> timestamps;

    private ManagedFunctionState(
        ImmutableMap<K,V> values, Timestamp timestamp, ImmutableMap<K,Timestamp> timestamps)
    {
        this.values = values;
        this.timestamp  = timestamp;
        this.timestamps = timestamps;
    }
}

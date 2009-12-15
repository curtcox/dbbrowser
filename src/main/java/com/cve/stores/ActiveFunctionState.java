package com.cve.stores;

import com.cve.util.Timestamp;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 *
 * @author curt
 */
@Immutable
public final class ActiveFunctionState<K,V> {

    final Timestamp timestamp;

    final ImmutableMap<K,V> values;

    final ImmutableMap<K,Timestamp> timestamps;

    private ActiveFunctionState(
        ImmutableMap<K,V> values, Timestamp timestamp, ImmutableMap<K,Timestamp> timestamps)
    {
        this.values = values;
        this.timestamp  = timestamp;
        this.timestamps = timestamps;
    }
}

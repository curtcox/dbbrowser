package com.cve.stores;

import com.cve.stores.fs.MemoryFSServersStore;
import com.cve.stores.db.MemoryDBServersStore;
import com.cve.stores.db.MemoryDBHintsStore;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.cve.model.db.Hints;
import com.cve.model.fs.FSConnectionInfo;
import com.cve.model.fs.FSServer;
import com.cve.stores.Store.Factory;
import com.cve.stores.db.DBHintsStore;
import com.cve.stores.db.DBServersStore;
import com.cve.stores.fs.FSServersStore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * A memory-only implementation of Store.Factory.  This is useful for testing,
 * debugging, benchmarking, etc...
 * @author Curt
 */
public final class MemoryStoreFactory implements Store.Factory {


    public static Factory of() {
        return new MemoryStoreFactory();
    }

    @Override
    public <K, V> Store<K, V> of(Class k, Class v) {
        return new MemoryStore();
    }

    @Override
    public <T> T of(Class<T> t) {
        if (t.equals(DBServersStore.class)) {
            return t.cast(MemoryDBServersStore.of());
        }
        if (t.equals(DBHintsStore.class)) {
            return t.cast(MemoryDBHintsStore.of());
        }
        if (t.equals(FSServersStore.class)) {
            return t.cast(new MemoryFSServersStore());
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }


    private static class MemoryStore implements Store {
        final Map map = Maps.newHashMap();
        @Override public Object get(Object key) { return map.get(key); }
        @Override public void put(Object key, Object value) { map.put(key, value); }
        @Override public ImmutableList keys() { return ImmutableList.copyOf(map.keySet()); }
    }

}

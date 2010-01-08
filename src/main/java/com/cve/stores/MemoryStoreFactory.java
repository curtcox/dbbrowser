package com.cve.stores;

import com.cve.db.DBColumn;
import com.cve.db.DBConnectionInfo;
import com.cve.db.DBServer;
import com.cve.db.Hints;
import com.cve.fs.FSConnectionInfo;
import com.cve.fs.FSServer;
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
            return t.cast(new DBServers());
        }
        if (t.equals(DBHintsStore.class)) {
            return t.cast(new DBHints());
        }
        if (t.equals(FSServersStore.class)) {
            return t.cast(new FSServers());
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }


    private static class MemoryStore implements Store {
        final Map map = Maps.newHashMap();
        @Override public Object get(Object key) { return map.get(key); }
        @Override public void put(Object key, Object value) { map.put(key, value); }
        @Override public ImmutableList keys() { return ImmutableList.copyOf(map.keySet()); }
    }

    private static class DBServers implements DBServersStore {
        final Map<DBServer,DBConnectionInfo> map = Maps.newHashMap();
        @Override public DBConnectionInfo get(DBServer key) { return map.get(key); }
        @Override public void put(DBServer key, DBConnectionInfo value) { map.put(key, value); }
        @Override public ImmutableList<DBServer> keys() { return ImmutableList.copyOf(map.keySet()); }
    }

    private static class DBHints implements DBHintsStore {
        final Map<ImmutableList<DBColumn>,Hints> map = Maps.newHashMap();
        @Override public Hints get(ImmutableList<DBColumn> key) { return map.get(key); }
        @Override public void put(ImmutableList<DBColumn> key, Hints value) { map.put(key, value); }
        @Override public ImmutableList<ImmutableList<DBColumn>> keys() { return ImmutableList.copyOf(map.keySet()); }
    }

        private static class FSServers implements FSServersStore {
        final Map<FSServer,FSConnectionInfo> map = Maps.newHashMap();
        @Override public FSConnectionInfo get(FSServer key) { return map.get(key); }
        @Override public void put(FSServer key, FSConnectionInfo value) { map.put(key, value); }
        @Override public ImmutableList<FSServer> keys() { return ImmutableList.copyOf(map.keySet()); }
    }

}

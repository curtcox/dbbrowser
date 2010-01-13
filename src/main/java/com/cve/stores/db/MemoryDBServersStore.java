package com.cve.stores.db;

import com.cve.model.db.DBConnectionInfo;
import com.cve.model.db.DBServer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Map;

public final class MemoryDBServersStore implements DBServersStore {

    final Map<DBServer, DBConnectionInfo> map = Maps.newHashMap();

    private MemoryDBServersStore() {}
    
    public static MemoryDBServersStore of() {
        return new MemoryDBServersStore();
    }

    @Override
    public DBConnectionInfo get(DBServer key) {
        return map.get(key);
    }

    @Override
    public void put(DBServer key, DBConnectionInfo value) {
        map.put(key, value);
    }

    @Override
    public ImmutableList<DBServer> keys() {
        return ImmutableList.copyOf(map.keySet());
    }
}

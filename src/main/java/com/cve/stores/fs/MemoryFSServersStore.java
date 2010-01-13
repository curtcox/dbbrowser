package com.cve.stores.fs;

import com.cve.model.fs.FSConnectionInfo;
import com.cve.model.fs.FSServer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Map;

public final class MemoryFSServersStore implements FSServersStore {

    final Map<FSServer, FSConnectionInfo> map = Maps.newHashMap();

    @Override
    public FSConnectionInfo get(FSServer key) {
        return map.get(key);
    }

    @Override
    public void put(FSServer key, FSConnectionInfo value) {
        map.put(key, value);
    }

    @Override
    public ImmutableList<FSServer> keys() {
        return ImmutableList.copyOf(map.keySet());
    }
}

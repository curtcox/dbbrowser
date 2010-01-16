package com.cve.stores.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.Hints;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Map;

public final class MemoryDBHintsStore implements DBHintsStore {

    final Map<ImmutableList<DBColumn>, Hints> map = Maps.newHashMap();

    private MemoryDBHintsStore() {}

    public static MemoryDBHintsStore of() {
        return new MemoryDBHintsStore();
    }

    @Override
    public Hints get(ImmutableList<DBColumn> key) {
        Hints hints = map.get(key);
        if (hints==null) {
            return Hints.NONE;
        }
        return hints;
    }

    @Override
    public void put(ImmutableList<DBColumn> key, Hints value) {
        map.put(key, value);
    }

    @Override
    public ImmutableList<ImmutableList<DBColumn>> keys() {
        return ImmutableList.copyOf(map.keySet());
    }
}

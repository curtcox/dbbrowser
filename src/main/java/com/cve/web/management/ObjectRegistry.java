package com.cve.web.management;

import com.cve.util.SimpleCache;
import com.google.common.collect.ImmutableList;
import java.util.Map;

/**
 * Global map of registered objects.
 */
public final class ObjectRegistry {

    /**
     * We use a simple cache to keep from running out of memory.
     * We can't use anything like a WeakHashMap, because the references will
     * be on the page in the user's browser.
     */
    static final Map<UniqueObjectKey,Object> objects = SimpleCache.of();

    static Object get(UniqueObjectKey key) {
        if (key==UniqueObjectKey.NULL) {
            return null;
        }
        Object o = objects.get(key);
        return o;
    }

    public static ImmutableList index() {
        return ImmutableList.copyOf(objects.keySet());
    }

    public static ImmutableList values() {
        return ImmutableList.copyOf(objects.values());
    }

    public static UniqueObjectKey put(Object o) {
        if (o==null) {
            return UniqueObjectKey.NULL;
        }
        UniqueObjectKey key = UniqueObjectKey.of(o);
        objects.put(key, o);
        return key;
    }

}

package com.cve.util;

import com.cve.lang.Memory;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple thread-safe LRU cache.
 * The cache contents will be discarded when memory is scarce, but this cache
 * will always retain at least one item.
 * @author Curt
 */
public final class SimpleCache<K,V> implements Map<K,V> {

    /**
     * Minimum number of entries a cache will hold before any are evicted.
     * Don't set this below 1.  Some cache usage depends on it being able to
     * store at least one thing, no matter what.
     */
    private final int min;

    /**
     * Maximum number of entries a cache can ever hold.
     */
    private static final int MAX = 1000;

    private final Lock lock = new ReentrantLock();

    private final LinkedHashMap<K,V> map = new LinkedHashMap() {
        // Oddly enough, the Javadoc seems to indicate that you can't just
        // specify a maximum size in the constructor.
        @Override
       protected boolean removeEldestEntry(Map.Entry eldest) {
            int size = size();
           return size > MAX || (size > min && Memory.isLow());
       }
    };

    /**
     * Use the factory.
     */
    private SimpleCache(int min) {
        this.min = min;
        if (min<1) {
            throw new IllegalArgumentException(min + " < 1");
        }
    }

    public static SimpleCache of() {
        return new SimpleCache(1000);
    }

    public Map copy() {
        lock.lock();
        try {
             return ImmutableMap.copyOf(map);
        } finally {
             lock.unlock();
        }
    }
    
    @Override
    public int size() {
        lock.lock();
        try {
            return map.size();
        } finally {
             lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return map.isEmpty();
        } finally {
             lock.unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        lock.lock();
        try {
            return map.containsKey(key);
        } finally {
             lock.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.lock();
        try {
            return map.containsValue(value);
        } finally {
             lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return map.get(key);
        } finally {
             lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            V result = map.put(key, value);
            return result;
        } finally {
             lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            return map.remove(key);
        } finally {
             lock.unlock();
        }
    }

    @Override
    public void putAll(Map m) {
        lock.lock();
        try {
            map.putAll(m);
        } finally {
             lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            map.clear();
        } finally {
             lock.unlock();
        }
    }

    // These unsupported methods have usage implications that need to be
    // thought through with respect to modifciation during iteration
    @Override
    public Set keySet() {
        lock.lock();
        try {
            return map.keySet();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Collection values() {
        lock.lock();
        try {
            return map.values();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

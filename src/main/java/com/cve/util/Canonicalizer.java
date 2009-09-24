package com.cve.util;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * For making sure you only keep one of several equivalent objects.
 * This is typically used by factory methods to avoid creating numerous
 * equivalent objects.
 * @author curt
 */
public final class Canonicalizer<T> {

    private final Map<T,T> map = Maps.newHashMap();

    private Canonicalizer() {}

    public static Canonicalizer of() {
        return new Canonicalizer();
    }
    
    public T canonical(T t) {
        if (t==null) {
            return null;
        }
        synchronized (map) {
            T value = map.get(t);
            if (value!=null) {
                return value;
            }
            map.put(t, t);
            return t;
        }
    }
}

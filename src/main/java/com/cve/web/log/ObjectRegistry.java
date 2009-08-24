package com.cve.web.log;

import com.google.common.collect.ImmutableList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gloabl map of registered objects.
 */
final class ObjectRegistry {

    /**
     * We might want to replace this in the future, with something like
     * the MD5 of the serialized bytes.
     */
    static class Key {

        final int value;

        static final Key NULL = new Key();

        Key() { value = 0; }

        Key(Object o) {
            value = System.identityHashCode(o);
        }

        static Key of(Object o) {
            if (o==null) {
                return NULL;
            }
            return new Key(o);
        }

        String toHexString() {
            return Integer.toHexString(value);
        }

        static Key parse(String string) {
            int code = Integer.parseInt(string,16);
            return new Key(code);
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            Key key = (Key) other;
            return value == key.value;
        }
    } // Key

    static final Map<Key,Object> objects = new ConcurrentHashMap<Key,Object>();

    static {
        put(ObjectRegistry.class);
    }

    static Object get(Key key) {
        return objects.get(key);
    }

    static ImmutableList index() {
        return ImmutableList.copyOf(objects.values());
    }

    static Key put(Object o) {
        Key key = Key.of(o);
        objects.put(key, o);
        return key;
    }

}

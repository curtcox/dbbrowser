package com.cve.web.log;

import com.google.common.collect.ImmutableList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gloabl map of registered objects.
 */
public final class ObjectRegistry {

    /**
     * We might want to replace this in the future, with something like
     * the MD5 of the serialized bytes.
     */
    public static class Key {

        final int value;

        static final Key NULL = new Key();

        private Key() { value = 0; }

        private Key(Object o) {
            value = System.identityHashCode(o);
        }

        private Key(int value) {
            this.value = value;
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

        @Override
        public String toString() {
            return Integer.toHexString(value);
        }
    } // Key

    static final Map<Key,Object> objects = new ConcurrentHashMap<Key,Object>();

    static Object get(Key key) {
        if (key==Key.NULL) {
            return null;
        }
        Object o = objects.get(key);
        return o;
    }

    static ImmutableList index() {
        return ImmutableList.copyOf(objects.values());
    }

    public static Key put(Object o) {
        if (o==null) {
            return Key.NULL;
        }
        Key key = Key.of(o);
        objects.put(key, o);
        return key;
    }

}

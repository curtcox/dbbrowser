package com.cve.web.management;

import com.cve.lang.Longs;
import java.math.BigInteger;
import javax.annotation.concurrent.Immutable;

/**
 * Two unique object keys should only match if they were produced from
 * equivalent objects.
 * <p>
 * For now, it is just based on the hash code of the object and its class.
 * This is quick, easy, and should be good enough.
 * We might want to replace this in the future, with something like
 * the MD5 of the serialized bytes of both.
 */
@Immutable
public class UniqueObjectKey {

    /**
     * The class the value is a member of.
     */
    public final int clazz;

    /**
     * The hash code of the value
     */
    public final int hash;

    /**
     * class | hash
     */
    public final long hash64;

    /**
     * Use this in place of null.
     */
    static final UniqueObjectKey NULL = new UniqueObjectKey();

    /**
     * Just for NULL.
     */
    private UniqueObjectKey() {
        clazz = 0;
        hash = 0;
        hash64 = 0;

    }

    /**
     * Use a factory.
     */
    private UniqueObjectKey(Object o) {
        clazz = o.getClass().hashCode();
        hash  = o.hashCode();
        hash64 = Longs.fromInts(clazz,hash);
    }

    private UniqueObjectKey(int clazz, int hash, long hash64) {
        this.clazz = clazz;
        this.hash  = hash;
        this.hash64 = hash64;
    }

    static UniqueObjectKey of(Object o) {
        if (o == null) {
            return NULL;
        }
        return new UniqueObjectKey(o);
    }

    public String toHexString() {
        return Long.toHexString(hash64);
    }

    static UniqueObjectKey parse(String string) {
        long hash64 = parseLong(string);
        int[] a = Longs.toInts(hash64);
        int clazz = a[0];
        int hash  = a[1];
        return new UniqueObjectKey(clazz,hash,hash64);
    }

    static long parseLong(String string) {
        // long hash64 = Long.parseLong(string, 16);
        // The code above will throw an exception for large enough unsigned longs
        // like Long.toHexString() will produce.
        return new BigInteger(string,16).longValue();
    }

    @Override
    public int hashCode() {
        return clazz ^ hash;
    }

    @Override
    @SuppressWarnings(value = "EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        UniqueObjectKey other = (UniqueObjectKey) o;
        return hash64==other.hash64;
    }

    @Override
    public String toString() {
        return Long.toHexString(hash64);
    }

}

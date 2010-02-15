package com.cve.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Handy static methods for dealing with objects.
 * @author curt
 */
public final class Objects {

    /**
     * Return true if both objects are null, or the first object returns
     * true for equals.
     */
    public static boolean equals(Object a, Object b) {
        if (a==null && b==null) {
            return true;
        }
        if (a!=null) {
            return a.equals(b);
        }
        if (a==null && b!=null) {
            return false;
        }
        throw new IllegalStateException();
    }

    /**
     * Return the size, in bytes, of the given object.
     * @return
     */
    public static long sizeOf(Object o) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream oOut = new ObjectOutputStream(bytes);
        oOut.writeObject(o);
        oOut.close();
        return bytes.toByteArray().length;
    }
}

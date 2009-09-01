package com.cve.web;

import com.cve.util.Check;
import javax.annotation.concurrent.Immutable;

/**
 * Wraps a byte array to produce a model.
 */
@Immutable
final class ByteArrayModel implements Model {

    private final byte[] a;

    final ContentType type;

    private ByteArrayModel(byte[] a, ContentType type) {
        this.a = copy(Check.notNull(a));
        this.type = Check.notNull(type);
    }

    static ByteArrayModel bytesType(byte[] a, ContentType type) {
        return new ByteArrayModel(a,type);
    }

    byte[] getBytes() {
        return copy(a);
    }

    private static byte[] copy(byte[] a) {
        byte[] b = new byte[a.length];
        for (int i=0; i<a.length; i++) {
            b[i] = a[i];
        }
        return b;
    }
}

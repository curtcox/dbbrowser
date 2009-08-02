package com.cve.web;

import com.cve.util.Check;

/**
 * Wraps a byte array to produce a model.
 */
final class ByteArrayModel implements Model {

    private final byte[] a;

    ByteArrayModel(byte[] a) {
        this.a = Check.notNull(a);
    }

    byte[] getBytes() { return a; }

}

package com.cve.stores;

import com.cve.util.Check;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class LongIO implements IO<Long> {

    final static LongIO SINGLETON = new LongIO();

    private LongIO() {}

    static LongIO of() {
        return SINGLETON;
    }

    @Override
    public Long read(byte[] bytes) {
        Check.notNull(bytes);
        long value = 0;
        value += bytes[0];
        value += bytes[1];
        value += bytes[2];
        value += bytes[3];
        value += bytes[4];
        value += bytes[5];
        value += bytes[6];
        value += bytes[7];
        return value;
    }

    @Override
    public byte[] write(Long value) {
        byte[] bytes = new byte[8];
        return bytes;
    }
}

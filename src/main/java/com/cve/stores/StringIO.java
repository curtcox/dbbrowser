package com.cve.stores;

import com.cve.util.Check;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class StringIO implements IO<String> {

    final static StringIO SINGLETON = new StringIO();

    private StringIO() {}

    static StringIO of() {
        return SINGLETON;
    }
    
    @Override
    public byte[] format(String string) {
        return Check.notNull(string.getBytes());
    }

    @Override
    public String parse(byte[] bytes) {
        return Check.notNull(new String(bytes));
    }


}

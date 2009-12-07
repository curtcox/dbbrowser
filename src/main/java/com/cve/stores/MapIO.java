package com.cve.stores;

import com.cve.util.Check;
import java.util.Map;

/**
 * For reading and writing key value pairs to and from files.
 * Things that we persist are stored as maps.
 */
public final class MapIO implements IO<Map> {

    final IO keyIO;
    final IO valueIO;
    final static IO longIO = LongIO.of();

    static MapIO of(IO keyIO, IO valueIO) {
        return new MapIO(keyIO,valueIO);
    }

    private MapIO(IO keyIO, IO valueIO) {
        this.keyIO = keyIO;
        this.valueIO = valueIO;
    }

    @Override
    public Map parse(byte[] line) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] format(Map value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

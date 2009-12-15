package com.cve.stores;

import com.cve.util.Check;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class ListIO implements IO<List> {

    final IO valueIO;
    static final IO longIO = LongIO.of();

    private ListIO(IO valueIO) {
        this.valueIO = valueIO;
    }

    static ListIO of(IO parser) {
        return new ListIO(parser);
    }
    
    @Override
    public List read(byte[] bytes) {
        List list = Lists.newArrayList();
        return list;
    }

    @Override
    public byte[] write(List list) {
        ByteWriter writer = ByteWriter.of();
        writer.add(longIO.write(list.size()));
        return writer.toBytes();
    }
}

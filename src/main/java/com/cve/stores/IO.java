package com.cve.stores;

import com.cve.util.Check;

/**
 * For reading and writing key value pairs to and from files.
 * Things that we persist are stored as maps.
 * Each map is persisted to a single file.
 */
public final class IO<K, V> {

    private final IOParser<K> keyParser;
    private final IOParser<V> valueParser;
    private final IOFormatter<K> keyFormatter;
    private final IOFormatter<V> valueFormatter;

    interface IOParser<T> {
        T parse(String line);
    }

    interface IOFormatter<T> {
       String format(T value);
    }

    private IO(IOParser keyParser, IOFormatter keyFormatter, IOParser valueParser, IOFormatter valueFormatter) {
        this.keyParser = Check.notNull(keyParser);
        this.keyFormatter = Check.notNull(keyFormatter);
        this.valueParser = Check.notNull(valueParser);
        this.valueFormatter = Check.notNull(valueFormatter);
    }

    K readKey(String line) {
        return keyParser.parse(line);
    }

    V readValue(String line) {
        return valueParser.parse(line);
    }

    String writeKey(K key) {
        return keyFormatter.format(key);
    }

    String writeValue(V value) {
        return valueFormatter.format(value);
    }

}

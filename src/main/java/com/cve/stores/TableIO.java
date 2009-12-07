package com.cve.stores;

import com.cve.db.DBTable;
import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.URIs;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class TableIO implements IO<DBTable> {

    final IO<String> stringIO = StringIO.of();

    final static TableIO SINGLETON = new TableIO();

    private TableIO() {}

    static TableIO of() {
        return SINGLETON;
    }

    @Override
    public byte[] format(DBTable value) {
        return stringIO.format(value.toString());
    }

    @Override
    public DBTable parse(byte[] bytes) {
        String line = stringIO.parse(bytes);
        String[]    parts = Check.notNull(line).split(".");
        return Server.uri(URIs.of(parts[0]))
                .databaseName(parts[1])
                .tableName(parts[2]);
    }

}

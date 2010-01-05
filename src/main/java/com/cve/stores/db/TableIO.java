package com.cve.stores.db;

import com.cve.db.DBTable;
import com.cve.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
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
    public byte[] write(DBTable value) {
        return stringIO.write(value.toString());
    }

    @Override
    public DBTable read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[]    parts = Check.notNull(line).split(".");
        return DBServer.uri(URIs.of(parts[0]))
                .databaseName(parts[1])
                .tableName(parts[2]);
    }

}

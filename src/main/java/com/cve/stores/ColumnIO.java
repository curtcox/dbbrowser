package com.cve.stores;

import com.cve.db.DBColumn;
import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.URIs;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class ColumnIO implements IO<DBColumn> {

    final IO<String> stringIO = StringIO.of();

    final static ColumnIO SINGLETON = new ColumnIO();

    private ColumnIO() {}

    static ColumnIO of() {
        return SINGLETON;
    }

    @Override
    public byte[] write(DBColumn value) {
        return stringIO.write(value.toString());
    }

    @Override
    public DBColumn read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[] parts = Check.notNull(line).split(".");
        return Server.uri(URIs.of(parts[0]))
               .databaseName(parts[1])
               .tableName(parts[2])
               .columnName(parts[3]);
    }

}

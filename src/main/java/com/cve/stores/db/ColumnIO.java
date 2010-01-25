package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
import static com.cve.util.Check.notNull;
import com.cve.util.URIs;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class ColumnIO implements IO<DBColumn> {

    final Log log;

    final IO<String> stringIO = StringIO.of();

    private ColumnIO(Log log) {
        this.log = notNull(log);
    }

    static ColumnIO of(Log log) {
        return new ColumnIO(log);
    }

    @Override
    public byte[] write(DBColumn value) {
        return stringIO.write(value.toString());
    }

    @Override
    public DBColumn read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[] parts = notNull(line).split(".");
        return DBServer.uri(URIs.of(parts[0]),log)
               .databaseName(parts[1])
               .tableName(parts[2])
               .columnName(parts[3]);
    }

}

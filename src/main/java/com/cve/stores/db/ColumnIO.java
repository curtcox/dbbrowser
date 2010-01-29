package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.log.Logs;
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

    final Log log = Logs.of();

    final IO<String> stringIO = StringIO.of();

    private ColumnIO() {
        
    }

    static ColumnIO of() {
        return new ColumnIO();
    }

    @Override
    public byte[] write(DBColumn value) {
        return stringIO.write(value.toString());
    }

    @Override
    public DBColumn read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[] parts = notNull(line).split(".");
        return DBServer.uri(URIs.of(parts[0]))
               .databaseName(parts[1])
               .tableName(parts[2])
               .columnName(parts[3]);
    }

}

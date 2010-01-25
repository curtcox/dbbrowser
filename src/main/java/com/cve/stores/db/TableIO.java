package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBTable;
import com.cve.model.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
import com.cve.util.Check;
import com.cve.util.URIs;
import static com.cve.util.Check.notNull;

/**
 * For formatting strings into strings.
 * @author Curt
 */
final class TableIO implements IO<DBTable> {

    final Log log;

    final IO<String> stringIO = StringIO.of();

    private TableIO(Log log) {
        this.log = notNull(log);
    }

    static TableIO of(Log log) {
        return new TableIO(log);
    }

    @Override
    public byte[] write(DBTable value) {
        return stringIO.write(value.toString());
    }

    @Override
    public DBTable read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[]    parts = Check.notNull(line).split(".");
        return DBServer.uri(URIs.of(parts[0]),log)
                .databaseName(parts[1])
                .tableName(parts[2]);
    }

}

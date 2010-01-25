package com.cve.stores.db;

import com.cve.log.Log;
import com.cve.model.db.DBColumn;
import com.cve.model.db.Join;
import com.cve.model.db.DBServer;
import com.cve.stores.IO;
import com.cve.stores.StringIO;
import static com.cve.util.Check.notNull;
import com.cve.util.URIs;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class JoinIO implements IO<Join> {

    final Log log;

    final IO<String> stringIO = StringIO.of();

    private JoinIO(Log log) {
        this.log = notNull(log);
    }

    static JoinIO of(Log log) {
        return new JoinIO(log);
    }

    @Override
    public Join read(byte[] bytes) {
        String line = stringIO.read(bytes);
        String[] parts = notNull(line).split(".");
        DBColumn source = DBServer.uri(URIs.of(parts[0]),log)
                .databaseName(line)
                .tableName(line)
                .columnName(line);
        DBColumn dest = DBServer.uri(URIs.of(parts[0]),log)
                .databaseName(line)
                .tableName(line)
                .columnName(line);
        return Join.of(source, dest);
    }

    @Override
    public byte[] write(Join value) {
        return stringIO.write(notNull(value).toString());
    }

}

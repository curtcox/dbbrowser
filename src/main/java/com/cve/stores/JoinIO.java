package com.cve.stores;

import com.cve.db.DBColumn;
import com.cve.db.Join;
import com.cve.db.Server;
import com.cve.util.Check;
import com.cve.util.URIs;

/**
 * For parsing strings into strings.
 * @author Curt
 */
final class JoinIO implements IO<Join> {

    final IO<String> stringIO = StringIO.of();

    final static JoinIO SINGLETON = new JoinIO();

    private JoinIO() {}

    static JoinIO of() {
        return SINGLETON;
    }

    @Override
    public Join parse(byte[] bytes) {
        String line = stringIO.parse(bytes);
        String[] parts = Check.notNull(line).split(".");
        DBColumn source = Server.uri(URIs.of(parts[0]))
                .databaseName(line)
                .tableName(line)
                .columnName(line);
        DBColumn dest = Server.uri(URIs.of(parts[0]))
                .databaseName(line)
                .tableName(line)
                .columnName(line);
        return Join.of(source, dest);
    }

    @Override
    public byte[] format(Join value) {
        return stringIO.format(Check.notNull(value).toString());
    }

}

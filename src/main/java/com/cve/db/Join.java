package com.cve.db;

import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A condition that joins {@link Row}S in two {@link Table}S based upon one
 * {@link Column} in each.
 * A join corresponds to a {@link SQL} where clause that involves two
 * {@link Column}S.
 * See also {@link Filter}
 */
@Immutable

public final class Join {

    private final DBColumn source;
    private final DBColumn dest;

    private Join(DBColumn source, DBColumn dest) {
        this.source = notNull(source);
        this.dest   = notNull(dest);
    }

    public static Join of(DBColumn source, DBColumn dest) {
        return new Join(source,dest);
    }

    public static Join parse(Server server, ImmutableList<DBTable> tables, String fullJoinName) {
        notNull(server);
        notNull(fullJoinName);
        String[]  nameParts = fullJoinName.split("\\=");
        if (nameParts.length!=2) {
            String message = fullJoinName + " is not of the form database1.table1.column1=database2.table2.column2";
            throw new IllegalArgumentException(message);
        }
        DBColumn source = DBColumn.parse(server,tables,nameParts[0]);
        DBColumn   dest = DBColumn.parse(server,tables,nameParts[1]);
        Join     join = Join.of(source, dest);
        return join;
    }

    public DBColumn getSource() { return source; }
    public DBColumn   getDest() { return dest;   }

    @Override
    public int hashCode() { return source.hashCode() ^ dest.hashCode(); }

    @Override
    public boolean equals(Object o) {
        Join other = (Join) o;
        return source.equals(other.source) && dest.equals(other.dest);
    }

    @Override
    public String toString() {
        return "source=" + source + " dest=" + dest;
    }
}

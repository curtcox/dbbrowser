package com.cve.db;

import com.cve.util.Canonicalizer;
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

    /**
     * From where
     */
    public final DBColumn source;

    /**
     * To where
     */
    public final DBColumn dest;

    private static final Canonicalizer<Join> CANONICALIZER = Canonicalizer.of();

    private static Join canonical(Join join) {
        return CANONICALIZER.canonical(join);
    }

    private Join(DBColumn source, DBColumn dest) {
        this.source = notNull(source);
        this.dest   = notNull(dest);
        if (source.equals(dest)) {
            throw new IllegalArgumentException(source + " used as both source and dest.");
        }
    }

    public static Join of(DBColumn source, DBColumn dest) {
        return canonical(new Join(source,dest));
    }

    public static Join parse(DBServer server, ImmutableList<DBTable> tables, String fullJoinName) {
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

    @Override
    public int hashCode() { return source.hashCode() ^ dest.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        Join other = (Join) o;
        return source.equals(other.source) && dest.equals(other.dest);
    }

    @Override
    public String toString() {
        return "source=" + source + " dest=" + dest;
    }
}

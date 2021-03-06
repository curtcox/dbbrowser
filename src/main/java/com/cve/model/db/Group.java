package com.cve.model.db;

import com.cve.util.Check;
import com.google.common.collect.ImmutableList;

/**
 * A group as in a SQL group by clause.
 * @author curt
 */
public final class Group {

    public final DBColumn column;

    /**
     * Use the factories.
     */
    private Group(DBColumn column) {
        this.column = Check.notNull(column);
    }

    /**
     * Factory method for creating OrderS.
     */
    public static Group of(DBColumn column) {
        return new Group(column);
    }

    @Override
    public int hashCode() { return column.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Group other = (Group) o;
        return column.equals(other.column);
    }

    @Override
    public String toString() {
        return "column=" + column;
    }

    public static Group parse(DBServer server, ImmutableList<DBTable> tables, String fullOrderName) {
        String[]  nameParts = fullOrderName.split("\\=");
        DBColumn     column = DBColumn.parse(server,tables,nameParts[0]);
        Group         group = Group.of(column);
        return group;
    }

}

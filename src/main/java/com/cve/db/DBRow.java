package com.cve.db;

import javax.annotation.concurrent.Immutable;

/**
 * A row in a {@link ResultSet}.
 */
@Immutable

public final class DBRow {

    private final int number;

    public static final DBRow FIRST = new DBRow(0);

    public static DBRow number(int number) {
        return new DBRow(number);
    }

    private DBRow(int number) {
        this.number = number;
    }

    public DBRow next() {
        return new DBRow(number + 1);
    }

    @Override
    public String toString() { return "row=" + number; }

    @Override
    public int hashCode() { return number; }

    @Override
    public boolean equals(Object o) {
        DBRow other = (DBRow) o;
        return number == other.number;
    }
}

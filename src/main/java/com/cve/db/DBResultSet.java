package com.cve.db;


import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableMap;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * The immutable results of running a {@link Select}.
 */
@Immutable

public final class DBResultSet {

    /**
     * The databases the data came from
     */
    public final ImmutableList<Database>  databases;

    /**
     * The tables the data came from
     */
    public final ImmutableList<DBTable>     tables;

    /**
     * The columns the data came from
     */
    public final ImmutableList<DBColumn>    columns;

    /**
     * The rows in the results
     */
    public final ImmutableList<DBRow>       rows;

    /**
     * The values in the results
     */
    public final ImmutableMap<Cell,DBValue> values;

    /**
     * Use this for a null result set.
     */
    public static final DBResultSet NULL = new DBResultSet(list(),list(),list(),list(),map());

    private DBResultSet(
        ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<DBRow> rows, ImmutableMap<Cell,DBValue> values)
    {
        this.databases = notNull(databases);
        this.tables    = notNull(tables);
        this.columns   = notNull(columns);
        this.rows      = notNull(rows);
        this.values    = notNull(values);
        validate();
    }

    /**
     * Make sure the map has an entry for every row and column
     */
    private void validate() {
        for (DBRow row : rows) {
            for (DBColumn column : columns) {
                Cell cell = Cell.at(row, column);
                DBValue value = values.get(cell);
                notNull(value,"Cell " + cell);
            }
        }
    }

    public static DBResultSet of(
        ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<DBRow> rows, ImmutableMap<Cell,DBValue> values)
    {
        return new DBResultSet(databases,tables,columns,rows,values);
    }

    public DBValue getValue(DBRow row, DBColumn column) {
        DBValue value = values.get(Cell.at(row,column));
        return notNull(value);
    }


    // A bunch of factory methods mainly for testing
    public static DBResultSet of(Database database, DBTable table, DBColumn column) {
        return new DBResultSet(list(database),list(table),list(column),list(),map());
    }

    public static DBResultSet of(Database database, DBTable table, DBColumn column, DBRow row, DBValue value) {
        ImmutableMap<Cell,DBValue> values = CellValues.of(row,column,value);
        return new DBResultSet(list(database),list(table),list(column),list(row),values);
    }

    public static DBResultSet of(Database database, DBTable table, DBColumn column, DBRow row, ImmutableMap<Cell,DBValue> values) {
        return new DBResultSet(list(database),list(table),list(column),list(row),values);
    }

    public static DBResultSet of(Database database, DBTable table, DBColumn column, ImmutableList<DBRow> rows, ImmutableMap<Cell,DBValue> values) {
        return new DBResultSet(list(database),list(table),list(column),rows,values);
    }

    public static DBResultSet of(Database database,
        DBTable t1, DBTable t2,
        DBColumn c1, DBColumn c2, DBColumn c3, DBColumn c4, DBRow r1, DBRow r2,
        ImmutableMap<Cell,DBValue> values) {
        return new DBResultSet(list(database),list(t1,t2),list(c1,c2,c3,c4),list(r1,r2),values);
    }

    private static ImmutableList             list() { return ImmutableList.of(); }
    private static <T> ImmutableList<T> list(T... items) { return ImmutableList.of(items); }
    private static ImmutableMap               map() { return ImmutableMap.of();  }

    @Override
    public int hashCode() {
        return databases.hashCode() ^ tables.hashCode() ^
                 columns.hashCode() ^   rows.hashCode() ^ values.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        DBResultSet other = (DBResultSet) o;
        return databases.equals(other.databases) &&
               tables.   equals(other.tables)    &&
               columns.  equals(other.columns)   &&
               rows.     equals(other.rows)      &&
               values.   equals(other.values);
    }

    @Override
    public String toString() {
        return " databases=" + databases   +
               " tables="    + tables      +
               " columns="   +  columns    +
               " #rows="     + rows.size() +
               " #values="   + values.size();
    }
}

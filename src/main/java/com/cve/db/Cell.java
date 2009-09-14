package com.cve.db;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A {@link Row}, {@link Column} pair.
 * It is actually a row, column, function triple.
 * That's because result sets can contain both c and f(c), for a given
 * aggregate function like count, min, max, etc...
 * Usually, the function is just the identity function, so it's mostly a
 * pair rather than a triple.
 * <p>
 * This is kind of ugly, but the only alternative I see is creating some sort
 * of object that could either be a column, or a column plus a function.
 * That would simplify {@link Select}, too.  Unfortunately, I can't see down
 * that road, but I think it would cause more problems than it solves.
 * A cell defines a value in a {@link ResultSet}.
 */
@Immutable
public final class Cell {

    /**
     * The row this cell is in.
     */
    public final DBRow row;

    /**
     * The column this cell is derived from.
     */
    public final DBColumn column;

    /**
     * The function that was applied to get this cell value.
     * This is usually just the identity function, but you never know.
     */
    public final AggregateFunction function;

    private Cell(DBRow row, DBColumn column, AggregateFunction function) {
        this.row      = notNull(row);
        this.column   = notNull(column);
        this.function = notNull(function);
    }

    public static Cell at(DBRow row, DBColumn column) {
        return new Cell(row,column,AggregateFunction.IDENTITY);
    }

    public static Cell at(DBRow row, DBColumn column, AggregateFunction function) {
        return new Cell(row,column,function);
    }

    @Override
    public int     hashCode() { return row.hashCode() ^ column.hashCode();  }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Cell cell = (Cell) o;
        return row.equals(cell.row) && column.equals(cell.column) && function.equals(cell.function);
    }

    @Override
    public String toString() {
        return "(" + row + "," + column + "," + function + ")";
    }
}

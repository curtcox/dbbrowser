package com.cve.db;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A {@link Row}, {@link Column} pair.
 * It is actually a row, column, function triple.
 * That's because result sets can contain both c and f(c), for a given
 * aggregate function like count, min, max, etc...
 * Usually, the function is just the identity function, so it's mostly a
 * pair.
 * <p>
 * This is kind of ugly, but the only alternative I see is creating some sort
 * of object that could either be a column, or a column plus a function.
 * That would simplify {@link Select}, too.  Unfortunately, I can't see down
 * that road, but I think it would cause more problems than it solves.
 * A cell defines a value in a {@link ResultSet}.
 */
@Immutable
public final class Cell {

    public final DBRow row;
    public final DBColumn column;
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

    public DBRow       getRow() { return row; }
    public DBColumn getColumn() { return column; }
    @Override
    public int     hashCode() { return row.hashCode() ^ column.hashCode();  }

    @Override
    public boolean equals(Object o) {
        Cell cell = (Cell) o;
        return row.equals(cell.row) && column.equals(cell.column) && function.equals(cell.function);
    }

    @Override
    public String toString() {
        return "(" + row + "," + column + "," + function + ")";
    }
}

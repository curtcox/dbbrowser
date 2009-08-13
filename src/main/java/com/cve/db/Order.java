package com.cve.db;

import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A SQL select statement order clause.
 * @author curt
 */
@Immutable
public final class Order {

    public final DBColumn column;
    public final Direction direction;

    /**
     * The direction of an order.  Ascending or descending.
     */
    public enum Direction {
        ASC,
        DESC;
    }

    /**
     * Use the factories.
     */
    private Order(DBColumn column, Direction direction) {
        this.column     = notNull(column);
        this.direction  = notNull(direction);
    }

    /**
     * Factory method for creating OrderS.
     */
    public static Order of(DBColumn column, Direction direction) {
        return new Order(column,direction);
    }

    /**
     * Factory method for creating OrderS.
     */
    public static Order ascending(DBColumn column) {
        return new Order(column,Direction.ASC);
    }

    @Override
    public int hashCode() { return column.hashCode() ^ direction.hashCode(); }

    @Override
    public boolean equals(Object o) {
        Order other = (Order) o;
        return column.equals(other.column) && direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "column=" + column + " " + direction;
    }

    public static Order parse(Server server, ImmutableList<DBTable> tables, String fullOrderName) {
        String[]  nameParts = fullOrderName.split("\\=");
        DBColumn             column = DBColumn.parse(server,tables,nameParts[0]);
        Order.Direction direction = Order.Direction.valueOf(nameParts[1]);
        Order               order = Order.of(column,direction);
        return order;
    }

}

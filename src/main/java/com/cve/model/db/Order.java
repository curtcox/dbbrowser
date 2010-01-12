package com.cve.model.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A SQL select statement order clause.
 * @author curt
 */
@Immutable
public final class Order {

    /**
     * The column this order applies to
     */
    public final DBColumn column;

    /**
     * The direction of the order.
     */
    public final Direction direction;

    /**
     * The direction of an order.  Ascending, descending, or none.
     */
    public enum Direction {
        /**
         * No imposed order
         */
        NONE,

        /**
         * Ascending
         */
        ASC,

        /**
         * Descending
         */
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

    /**
     * Return a similar list with only ascending and descending orders.
     */
    static ImmutableList<Order> normalize(ImmutableList<Order> orders) {
        List<Order> copy = Lists.newArrayList();
        for (Order order : orders) {
            if (order.direction!=Order.Direction.NONE) {
                copy.add(order);
            }
        }
        return ImmutableList.copyOf(copy);
    }


    @Override
    public int hashCode() { return column.hashCode() ^ direction.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Order other = (Order) o;
        return column.equals(other.column) && direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return "column=" + column + " " + direction;
    }

    public static Order parse(DBServer server, ImmutableList<DBTable> tables, String fullOrderName) {
        String[]  nameParts = fullOrderName.split("\\=");
        DBColumn             column = DBColumn.parse(server,tables,nameParts[0]);
        Order.Direction direction = Order.Direction.valueOf(nameParts[1]);
        Order               order = Order.of(column,direction);
        return order;
    }

}

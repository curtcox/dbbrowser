package com.cve.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.URIs;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A column in a database {@link Table}.
 */
@Immutable
public final class DBColumn {

    /**
     * The table this column is on.
     */
    public final DBTable  table;

    /**
     * The name of this column.
     */
    public final String name;

    /**
     * What kind of thing this column holds.
     * If the type has not been determined, the wildcard Void will be
     * used internally.
     */
    private final Class  type;

    /**
     * The column to use for "all columns".
     * This is used with count(*).
     */
    public static final DBColumn ALL = tableName(DBTable.NULL,"*");

    /**
     * Use the factories.
     */
    private DBColumn(DBTable table, String name, Class type) {
        this.table = notNull(table);
        this.name  = notNull(name);
        this.type  = notNull(type);
    }

    public static DBColumn tableNameType(DBTable table, String name, Class type) {
        return new DBColumn(table,name,type);
    }

    public static DBColumn tableName(DBTable table, String name) {
        return new DBColumn(table,name,Void.class);
    }

    public Filter filterValue(Value value) {
        return Filter.of(this, value);
    }

    public static DBColumn parse(Server server, ImmutableList<DBTable> tables, String fullColumnName) {
        notNull(server,fullColumnName);
        String[]  nameParts = fullColumnName.split("\\.");
        if (nameParts.length!=1 && nameParts.length!=3) {
            String message = fullColumnName +
                " is not of the form database.table.column, (impied table 0) column," +
                " or #column";
            throw new IllegalArgumentException(message);
        }
        if (nameParts.length==3) {
            String databaseName = nameParts[0];
            String    tableName = nameParts[1];
            String   columnName = nameParts[2];
            Database  database = server.databaseName(databaseName);
            DBTable        table = database.tableName(tableName);
            DBColumn      column = table.columnName(columnName);
            return column;
        }
        String shortName = nameParts[0];
        if (startsWithDigit(shortName)) {
            int digit = Integer.parseInt(shortName.substring(0,1));
            return tables.get(digit + 1).columnName(shortName.substring(1));
        }
        return tables.get(0).columnName(shortName);
    }

    private static boolean startsWithDigit(String string) {
        char c = string.charAt(0);
        return c == '0' || c == '9' || (c > '0' && c < '9');
    }

    /**
     * Return database.tableName.columnName.
     * So, full doesn't mean with server name.
     */
    public String fullName() {
        if (this==ALL) {
            return "*";
        }
        return table.database.name + "." + table.name + "." + name;
    }

    @Override
    public   int hashCode() { return table.hashCode() ^ name.hashCode() ^ type.hashCode(); }

    @Override
    public boolean equals(Object o) {
        DBColumn other = (DBColumn) o;
        return table.equals(other.table) && name.equals(other.name) && type.equals(other.type);
    }

    @Override
    public String toString() {
        return "name=" + name + " type=" + type + " table=" + table;
    }

    public Link linkTo() {
        Database database = table.database;
        Server     server = database.server;
        URI target = URIs.of(
            "/" + server.uri + "/" + database.name + "/" + table.fullName() + "/" + fullName() + "/"
        );
        Label text = Label.of(name);
        return Link.textTarget(text, target);
    }

}
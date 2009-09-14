package com.cve.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.web.db.DBURICodec;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * A table in a {@link Database}.
 */
@Immutable

public final class DBTable {

    /**
     * The database this table is on.
     */
    public final Database database;

    /**
     * The name of the table.
     */
    public final String name;

    /**
     * Something to use for null tables.
     */
    public static DBTable NULL = new DBTable(Database.NULL,"");

    private DBTable(Database database, String name) {
        this.database = notNull(database);
        this.name     = notNull(name);
    }

    public static DBTable databaseName(Database database, String name) {
        return new DBTable(database,name);
    }

    public static DBTable parse(Server server, String fullTableName) {
        args(server,fullTableName);
        notNull(server);
        notNull(fullTableName);
        String[]  nameParts = fullTableName.split("\\.");
        if (nameParts.length!=2) {
            String message = fullTableName + " is not of the form database.table";
            throw new IllegalArgumentException(message);
        }
        String databaseName = nameParts[0];
        String    tableName = nameParts[1];
        Database  database = server.databaseName(databaseName);
        return database.tableName(tableName);
    }


    public DBColumn columnNameType(String name, Class type) {
        return DBColumn.tableNameType(this, name, type);
    }

    public DBColumn columnName(String name) {
        return DBColumn.tableName(this, name);
    }

    public DBColumn keyColumnName(String name) {
        return DBColumn.keyTableName(this, name);
    }

    public DBColumn foreignkeyColumnName(String name) {
        return DBColumn.foreignkeyTableName(this, name);
    }

    public String fullName() { return database.name + "." + name; }

    @Override
    public String toString() { return fullName(); }

    @Override
    public int hashCode() { return database.hashCode() ^ name.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        DBTable other = (DBTable) o;
        return database.equals(other.database) && name.equals(other.name);
    }

    /**
     * Provide a link to this table.
     */
    public Link linkTo() {
        Label text = Label.of(name);
        URI target = DBURICodec.encode(this);
        return Link.textTarget(text, target);
    }
}

package com.cve.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.URIs;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A table in a {@link Database}.
 */
@Immutable

public final class DBTable {

    private final Database database;
    private final String name;

    public static DBTable NULL = new DBTable(Database.NULL,"");

    private DBTable(Database database, String name) {
        this.database = notNull(database);
        this.name     = notNull(name);
    }

    public static DBTable databaseName(Database database, String name) {
        return new DBTable(database,name);
    }

    public static DBTable parse(Server server, String fullTableName) {
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

    public String       getName() { return name; }
    public Database getDatabase() { return database; }

    public String fullName() { return database.getName() + "." + name; }

    @Override
    public String toString() { return fullName(); }

    @Override
    public int hashCode() { return database.hashCode() ^ name.hashCode(); }

    @Override
    public boolean equals(Object o) {
        DBTable other = (DBTable) o;
        return database.equals(other.database) && name.equals(other.name);
    }

    public Link linkTo() {
        Label text = Label.of(name);
        String databaseName = database.getName();
        Server server = database.getServer();
        URI target = URIs.of(
            "/" + server.getURI() + "/" + databaseName + "/" + fullName() + "/"
        );
        return Link.textTarget(text, target);
    }
}

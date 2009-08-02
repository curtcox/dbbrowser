package com.cve.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.URIs;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A relational database on a {@link Server}.
 */
@Immutable
public final class Database {

    private final String name;
    private final Server server;

    public static final Database NULL = new Database(Server.NULL,"");

    private Database(Server server, String name) {
        this.server = notNull(server);
        this.name   = notNull(name);
    }

    public static Database serverName(Server server, String name) {
        return new Database(server,name);
    }

    public String   getName() { return name; }
    public Server getServer() { return server; }

    @Override
    public   int hashCode() { return name.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        Database database = (Database) o;
        return name.equals(database.name);
    }

    public DBTable tableName(String name) {
        return DBTable.databaseName(this, name);
    }

    @Override
    public String toString() {
        return "name=" + name + " server=" + server;
    }

    public Link linkTo() {
        Label text = Label.of(name);
        URI target = URIs.of("/" + server.getURI() + "/" + name + "/");
        return Link.textTarget(text, target);
    }
}

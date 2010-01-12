package com.cve.model.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.Canonicalizer;
import com.cve.web.db.DBURICodec;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * A relational database on a {@link Server}.
 */
@Immutable
public final class Database {

    /**
     * The name of the database
     */
    public final String name;

    /**
     * The server the database is on
     */
    public final DBServer server;

    private static final Canonicalizer<Database> CANONICALIZER = Canonicalizer.of();

    public static final Database NULL = new Database(DBServer.NULL,"");

    private Database(DBServer server, String name) {
        this.server = notNull(server);
        this.name   = notNull(name);
    }

    private static Database canonical(Database database) {
        return CANONICALIZER.canonical(database);
    }

    public static Database serverName(DBServer server, String name) {
        return canonical(new Database(server,name));
    }

    @Override
    public   int hashCode() { return name.hashCode() ^ server.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        Database other = (Database) o;
        return name.equals(other.name) && server.equals(other.server);
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
        URI target = DBURICodec.encode(this);
        return Link.textTarget(text, target);
    }
}

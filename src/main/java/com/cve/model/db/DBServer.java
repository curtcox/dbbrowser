package com.cve.model.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Canonicalizer;
import com.cve.util.URIs;
import com.cve.web.db.DBURICodec;
import java.net.URI;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A {@link Database} server.
 */
@Immutable
public final class DBServer {

    public final Log log = Logs.of();

    /**
     * How the server is represented in URLs.
     * This isn't the URI that the server is at, but rather the URI fragment
     * that represents the server in our URIs.
     */
    public final URI uri;

    private static final Canonicalizer<DBServer> CANONICALIZER = Canonicalizer.of();

    public static DBServer NULL = new DBServer(URIs.of(""));


    private static DBServer canonical(DBServer server) {
        return CANONICALIZER.canonical(server);
    }

    private DBServer(URI uri) {
        this.uri = notNull(uri);
        
    }

    public static DBServer uri(URI uri) {
        return canonical(new DBServer(uri));
    }

    @Override
    public   int hashCode() { return uri.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        DBServer server = (DBServer) o;
        return uri.equals(server.uri);
    }

    public Database databaseName(String name) {
        return Database.serverName(this, name);
    }

    @Override
    public String toString() { return uri.toString(); }

    public Link linkTo() {
        Label text = Label.of(toString());
        URI target = DBURICodec.encode(this);
        return Link.textTarget(text, target);
    }
}

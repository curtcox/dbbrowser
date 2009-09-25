package com.cve.db;

import com.cve.html.Label;
import com.cve.html.Link;
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
public final class Server {

    /**
     * How the server is represented in URLs.
     */
    public final URI uri;

    private static final Canonicalizer<Server> CANONICALIZER = Canonicalizer.of();

    public static Server NULL = new Server(URIs.of(""));


    private static Server canonical(Server server) {
        return CANONICALIZER.canonical(server);
    }

    private Server(URI uri) {
        this.uri = notNull(uri);
    }

    public static Server uri(URI uri) {
        return canonical(new Server(uri));
    }

    @Override
    public   int hashCode() { return uri.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        Server server = (Server) o;
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

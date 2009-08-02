package com.cve.db;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.URIs;
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
    private final URI uri;

    public static Server NULL = new Server(URIs.of(""));

    private Server(URI uri) {
        this.uri = notNull(uri);
    }

    public static Server uri(URI uri) {
        return new Server(uri);
    }

    /**
     * Get how the server is represented in URLs.
     */
    public URI getURI() { return uri; }

    @Override
    public   int hashCode() { return uri.hashCode(); }

    @Override
    public boolean equals(Object o) {
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
        URI target = uri;
       return Link.textTarget(text, target);
    }
}

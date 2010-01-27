package com.cve.model.fs;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.util.Canonicalizer;
import com.cve.util.URIs;
import com.cve.web.fs.FSURICodec;
import java.net.URI;
import static com.cve.util.Check.notNull;

/**
 * A file server.
 * @author curt
 */
public final class FSServer {

    final Log log;

    /**
     * How the server is represented in URLs.
     * This isn't the URI that the server is at, but rather the URI fragment
     * that represents the server in our URIs.
     */
    public final URI uri;

    private static final Canonicalizer<FSServer> CANONICALIZER = Canonicalizer.of();

    public static FSServer NULL = new FSServer(URIs.of(""),null);


    private static FSServer canonical(FSServer server) {
        return CANONICALIZER.canonical(server);
    }

    private FSServer(URI uri, Log log) {
        this.uri = notNull(uri);
        this.log = notNull(log);
    }

    public static FSServer uri(URI uri, Log log) {
        return canonical(new FSServer(uri,log));
    }

    @Override
    public   int hashCode() { return uri.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        FSServer server = (FSServer) o;
        return uri.equals(server.uri);
    }

    public FSPath path(String path) {
        return FSPath.serverPath(this, path,log);
    }

    @Override
    public String toString() { return uri.toString(); }

    public Link linkTo() {
        Label text = Label.of(toString(),log);
        URI target = FSURICodec.encode(this);
        return Link.textTarget(text, target);
    }

}

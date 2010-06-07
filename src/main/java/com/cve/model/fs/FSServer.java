package com.cve.model.fs;

import com.cve.lang.URIObject;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Canonicalizer;
import com.cve.util.URIs;
import com.cve.web.fs.FSURICodec;

import static com.cve.util.Check.notNull;

/**
 * A file server.
 * @author curt
 */
public final class FSServer {

    final Log log = Logs.of();

    /**
     * How the server is represented in URLs.
     * This isn't the URIObject that the server is at, but rather the URIObject fragment
     * that represents the server in our URIs.
     */
    public final URIObject uri;

    private static final Canonicalizer<FSServer> CANONICALIZER = Canonicalizer.of();

    public static FSServer NULL = new FSServer(URIs.of(""));


    private static FSServer canonical(FSServer server) {
        return CANONICALIZER.canonical(server);
    }

    private FSServer(URIObject uri) {
        this.uri = notNull(uri);
        
    }

    public static FSServer uri(URIObject uri) {
        return canonical(new FSServer(uri));
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
        return FSPath.serverPath(this, path);
    }

    @Override
    public String toString() { return uri.toString(); }

    public Link linkTo() {
        Label text = Label.of(toString());
        URIObject target = FSURICodec.encode(this);
        return Link.textTarget(text, target);
    }

}

package com.cve.model.fs;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.util.Canonicalizer;
import com.cve.web.fs.FSURICodec;
import java.net.URI;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
public class FSPath {

    /**
     * The server this path is on.
     */
    public final FSServer server;

    /**
     * The path from the root.
     */
    public final String name;

    private static final Canonicalizer<FSPath> CANONICALIZER = Canonicalizer.of();

    /**
     * Something to use for null paths.
     */
    public static FSPath NULL = new FSPath(FSServer.NULL,"");

    private static FSPath canonical(FSPath table) {
        return CANONICALIZER.canonical(table);
    }

    private FSPath(FSServer server, String name) {
        this.server  = notNull(server);
        this.name    = notNull(name);
    }

    public static FSPath serverPath(FSServer server, String name) {
        return canonical(new FSPath(server,name));
    }

    public static FSPath parse(FSServer server, String path, Log log) {
        log.notNullArgs(server,path);
        notNull(server);
        notNull(path);
        return server.path(path);
    }

    public String fullName() { return server.uri + "." + name; }

    @Override
    public String toString() { return fullName(); }

    @Override
    public int hashCode() { return server.hashCode() ^ name.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        FSPath other = (FSPath) o;
        return server.equals(other.server) && name.equals(other.name);
    }

    /**
     * Provide a link to this table.
     */
    public Link linkTo() {
        Label text = Label.of(name);
        URI target = FSURICodec.encode(this);
        return Link.textTarget(text, target);
    }

}

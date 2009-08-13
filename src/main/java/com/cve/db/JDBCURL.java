package com.cve.db;

import java.net.URI;

import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A URL for a JDBC connection.
 */
@Immutable

public final class JDBCURL {

    /**
     * Get how the server is represented in URLs.
     */
    public final URI uri;

    private JDBCURL(URI uri) {
        this.uri = notNull(uri);
    }

    public static JDBCURL uri(URI uri) {
        return new JDBCURL(uri);
    }


    @Override
    public   int hashCode() { return uri.hashCode(); }

    @Override
    public boolean equals(Object o) {
        JDBCURL other = (JDBCURL) o;
        return uri.equals(other.uri);
    }

    @Override
    public String toString() { return uri.toString(); }
}

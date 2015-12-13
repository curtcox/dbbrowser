package com.cve.lang;

import java.net.URI;
import java.net.URISyntaxException;

public final class URIObject {

    final String target;

    public final static class Path {

        public static Path of(String string) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        private Path() {}
    }

    public URIObject(String target) throws URISyntaxException {
        this.target = target;
    }

    public URI toURI() {
        try {
            return new URI(target);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return target;
    }
}


package com.cve.util;

import java.net.URI;
import java.net.URISyntaxException;

import static com.cve.util.Check.notNull;

/**
 * Tools for working with {@link URI}S.
 */
public final class URIs {

    public static URI of(String target) {
        try {
            return new URI(target);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static int slashCount(String uri) {
        notNull(uri);
        int count = 0;
        for (int i=0; i<uri.length(); i++) {
            if ('/'==uri.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    public static URI startingAtSlash(String uri, int i) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

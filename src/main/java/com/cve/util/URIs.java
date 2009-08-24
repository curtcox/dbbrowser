
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

    /**
        // /view/CSV/foo
        //          ^ start here
     */
    public static URI startingAtSlash(String uri, int slash) {
        int j=0;
        for (int i=0; i<uri.length(); i++) {
            if (uri.charAt(i)=='/') {
                j++;
                if (j==slash+1) {
                    return URIs.of(uri.substring(i));
                }
            }
        }
        throw new IllegalArgumentException(uri + " starting at slash " + slash);
    }

}

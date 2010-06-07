
package com.cve.util;


import com.cve.lang.URIObject;
import java.net.URISyntaxException;

import static com.cve.util.Check.notNull;

/**
 * Tools for working with {@link URI}S.
 * What can you put in a URI?
 * xalpha --  alpha  digit safe extra escape
 * <ol>
       <li> alpha -- a - z, A - Z
       <li> digit -- 0 - 9
       <li> safe -- $  -  _  @  .  &   +  -
       <li> extra -- !  *   "   '  (  ) ,
       <li> reserved -- =  ;  /  #  ? : space
 * </ol>
 * So, the characters listed above as reserved can't be put in a URI.
 * See http://www.w3.org/Addressing/URL/url-spec.txt
 */
public final class URIs {

    public static URIObject of(String target) {
        try {
            return new URIObject(target);
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
    public static URIObject startingAtSlash(String uri, int slash) {
        int j=0;
        for (int i=0; i<uri.length(); i++) {
            if (uri.charAt(i)=='/') {
                j++;
                if (j==slash) {
                    return URIs.of(uri.substring(i));
                }
            }
        }
        throw new IllegalArgumentException(uri + " starting at slash " + slash);
    }

}

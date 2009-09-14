package com.cve.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * For enhanced URL encoding and decoding.
 * This is used for encoding and decoding ValueS in URLs.
 * Since encoded spaces (+) are used as second-level separators in our DB URLs,
 * (/ is the top level) this class makes sure the encoded verison doesn't
 * include any occurences of (+).
 * @author curt
 */
public final class URLCodec {

     public static String decode(String string) {
        return URLDecoder.decode(URLDecoder.decode(string));
    }

    public static String encode(String string) {
        return URLEncoder.encode(URLEncoder.encode(string));
    }
}

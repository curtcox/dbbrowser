package com.cve.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * For enhanced URL encoding and decoding.
 * @author curt
 */
public final class URLCodec {

     public static String decode(String string) {
        string = URLDecoder.decode(string);
        string = string.replace("_", " ");
        string = string.replace("%2B", "+");
        return string;
    }

    public static String encode(String string) {
        string = string.replace(" ", "_");
        string = string.replace("+", "%2B");
        return URLEncoder.encode(string);
    }
}

package com.cve.util;

import java.util.List;

/**
 * Static methods for dealing with strings.
 */
public final class Strings {

     public static String separated(List<String> list, String separator) {
        StringBuilder out = new StringBuilder();
        for (int i=0; i<list.size(); i++) {
            out.append(list.get(i));
            if (i<list.size() - 1) {
                out.append(separator);
            }
        }
        return out.toString();
    }
}

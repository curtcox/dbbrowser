package com.cve.lang;

import java.util.List;

/**
 * Static methods for dealing with strings.
 */
public final class Strings {

    public static int hashCode(String s) {
        if (s==null) {
            return 0;
        }
        return s.hashCode();
    }

    public static boolean equals(String a, String b) {
        if (a==null && b==null) {
            return true;
        }
        if (a==null & b!=null) {
            return false;
        }
        if (a!=null & b==null) {
            return false;
        }
        return a.equals(b);
    }

    /**
     * For things like generating CSV.
     * Return a string of the given strings, separated by the given separator.
     */
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

    /**
     * Return the given string, up to the given number of characters.
     * Return "null" instead of null, if given a null value.
     * Return a string ending with "..." if it needs to be truncated.
     */
    public static String first(int count, String s) {
        if (s==null) {
            return "null";
        }
        if (s.length() <= count) {
            return s;
        }
        return s.substring(0,count) + "...";
    }

    /**
     * Return true, if the given string is null or empty.
     */
    public static boolean isEmpty(String s) {
        if (s==null) {
            return true;
        }
        return s.trim().isEmpty();
    }

    /**
     * Return the portion of the string after the last occurence of the substring.
     */
    public static String afterLast(String s, String sub) {
        int at = s.lastIndexOf(sub);
        return s.substring(at + sub.length());
    }
}

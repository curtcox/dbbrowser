package com.cve.lang;

/**
 * Handy static methods for dealing with objects.
 * @author curt
 */
public final class Objects {

    /**
     * Return true if both objects are null, or the first object returns
     * true for equals.
     */
    public static boolean equals(Object a, Object b) {
        if (a==null && b==null) {
            return true;
        }
        if (a!=null) {
            return a.equals(b);
        }
        if (a==null && b!=null) {
            return false;
        }
        throw new IllegalStateException();
    }

}

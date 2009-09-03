package com.cve.web;

import com.cve.util.Check;

/**
 * A textual search, ala Google.
 * @author curt
 */
public final class Search {

    public final String target;

    private Search(String target) {
        this.target = Check.notNull(target);
    }

    public static Search of(String target) {
        return new Search(target);
    }

    @Override
    public int hashCode() { return target.hashCode(); }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        Search other = (Search) o;
        return target.equals(other.target);
    }
}

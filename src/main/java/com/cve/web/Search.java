package com.cve.web;

import com.cve.util.Check;

/**
 * A textual search, ala Google.
 * This is just a typesafe string.  How to interpret the string is up to
 * whatever is doing the searching.
 * @author curt
 */
public final class Search {

    /**
     * Description of what is being searched for.
     */
    public final String target;

    /**
     * The search to use when you don't want to search.
     */
    public static final Search EMPTY = new Search("");

    /**
     * Key for specifying searches
     */
    public static String FIND = "find";

    /**
     * Use the factories.
     */
    private Search(String target) {
        this.target = Check.notNull(target).trim();
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

    public boolean isEmpty() {
        return target.isEmpty();
    }

}

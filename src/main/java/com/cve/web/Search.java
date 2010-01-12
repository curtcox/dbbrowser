package com.cve.web;

import com.cve.util.Check;
import java.net.URLEncoder;

/**
 * A textual search, ala Google.
 * This is just a typesafe string.  How to interpret the string is up to
 * whatever is doing the searching.
 * <p>
 * See SearchRedirectsHandler
 * @author curt
 */
public final class Search {

    /**
     * What kind of thing is being searched.
     * This is used to disambiguate for cases like --
     * "Do you want to search the names of the columns and tables,
     * or all of the rows in all of the tables."
     *
     */
    public enum Space {

        /**
         * The names of this thing.  If it is a table, that means the table
         * and column names.
         */
        NAMES(""),

        /**
         * The contents of this thing.  If it is a table, that means the
         * values in the rows.
         */
        CONTENTS("*");

        final String prefix;

        Space(String prefix) {
            this.prefix = prefix;
        }
    }

    /**
     * Description of what is being searched for.
     */
    public final String target;

    /**
     * What is to be searched.
     */
    public final Space space;

    /**
     * The search to use when you don't want to search.
     */
    public static final Search EMPTY = new Search("",Space.CONTENTS);

    /**
     * Key for specifying searches
     */
    public static String FIND = "find";

    /**
     * Use the factories.
     */
    private Search(String target, Space space) {
        this.space  = Check.notNull(space);
        this.target = Check.notNull(target).trim();
    }

    public static Search of(String target, Space space) {
        return new Search(target,space);
    }

    public static Search contents(String target) {
        return new Search(target,Space.CONTENTS);
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

    public Search ofContents() {
        return Search.of(target,Space.CONTENTS);
    }

    public static Search parse(String string) {
        Check.notNull(string);
        if (string.startsWith(Space.CONTENTS.prefix)) {
            return Search.of(string.substring(1),Space.CONTENTS);
        }

        return Search.of(string,Space.NAMES);
    }

    /**
     * Constructs a URL fragment that will be used in a query string or in
     * the search section of a select URL.
     * See parse.
     */
    public String toUrlFragment() {
        // For an empty search, use "+" instead of "".
        // This is because HTTP URLs that start with // are taken to be relative,
        // with only the protocol specified.  So,
        //    //server/db -> http://server/db
        // but
        //    /+/server/db
        // still gets mapped to this server, like we want.
        if (isEmpty() && space==Space.NAMES) {
            return "+";
        }
        return space.prefix + URLEncoder.encode(target);
    }

}

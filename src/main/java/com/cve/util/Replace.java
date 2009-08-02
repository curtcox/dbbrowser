package com.cve.util;

import static com.cve.util.Check.notNull;
/**
 * To make constructing string with embedded quotes easier.
 */
public final class Replace {

    /**
     * Replace [ and ] with ".
     * This makes it easier to construct strings with lots of quotes.
     */
    public static String bracketQuote(String string) {
        notNull(string);
        return string.replace("[", "\"").replace("]", "\"");
    }

    /**
     * Replace [ and ] with ".
     * This makes it easier to construct strings with lots of quotes.
     */
    public static String bracketSingleQuote(String string) {
        notNull(string);
        return string.replace("[", "'").replace("]", "'");
    }

    /**
     * Replace " with &quot and ' with \'.
     * This is for tooltip HTML, which can't contain either of those.
     */
    public static String escapeQuotes(String string) {
        notNull(string);
        return string.replace("\"", "&quot;").replace("'", "\'");
    }

}

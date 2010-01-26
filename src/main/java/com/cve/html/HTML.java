package com.cve.html;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;
/**
 * A typesafe string for HMTL, plus some string-based static methods to make
 * constructing it easier.  Also, use of this class enables some debugging
 * functionality.
 */
public final class HTML {

    final Log log;

    private final String html;

    private HTML(String html, Log log) {
        this.html = notNull(html);
        this.log = notNull(log);
    }

    public static HTML of(String html,Log log) {
        return new HTML(html,log);
    }

    @Override
    public String toString() { return html; }

}

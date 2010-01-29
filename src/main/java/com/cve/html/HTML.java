package com.cve.html;

import com.cve.log.Log;
import com.cve.log.Logs;
import static com.cve.util.Check.notNull;
/**
 * A typesafe string for HMTL, plus some string-based static methods to make
 * constructing it easier.  Also, use of this class enables some debugging
 * functionality.
 */
public final class HTML {

    final Log log = Logs.of();

    private final String html;

    private HTML(String html) {
        this.html = notNull(html);
        
    }

    public static HTML of(String html) {
        return new HTML(html);
    }

    @Override
    public String toString() { return html; }

}

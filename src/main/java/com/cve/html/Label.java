package com.cve.html;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;
/**
 * A typesafe-wrapper for a label string.
 */
public final class Label {

    final Log log;
    
    private final String text;

    private Label(String text, Log log) {
        this.text = notNull(text);
        this.log = notNull(log);
    }

    public static Label of(String text, Log log) {
        return new Label(text,log);
    }
    
    @Override
    public String toString() { return text; }


}

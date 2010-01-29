package com.cve.html;

import com.cve.log.Log;
import com.cve.log.Logs;
import static com.cve.util.Check.notNull;
/**
 * A typesafe-wrapper for a label string.
 */
public final class Label {

    final Log log = Logs.of();
    
    private final String text;

    private Label(String text) {
        this.text = notNull(text);
        
    }

    public static Label of(String text) {
        return new Label(text);
    }
    
    @Override
    public String toString() { return text; }


}

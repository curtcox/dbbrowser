package com.cve.html;

import com.cve.log.Log;
import static com.cve.util.Check.notNull;

/**
 * For creating a tooltip when you already know the HTML.
 * This is just a typesafe wrapper for HTML.
 */
public final class SimpleTooltip
    implements Tooltip
{

    private final HTML html;

    private SimpleTooltip(HTML html) {
        this.html = notNull(html);
    }

    public static SimpleTooltip of(HTML html) {
        return new SimpleTooltip(html);
    }

    public static SimpleTooltip of(String html) {
        return new SimpleTooltip(HTML.of(html));
    }

    @Override
    public HTML toHTML() { return html; }

}

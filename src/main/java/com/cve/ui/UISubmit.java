package com.cve.ui;

import com.cve.util.Check;
import com.cve.util.Replace;
import java.net.URI;
import javax.annotation.concurrent.Immutable;

/**
 * Like a HTML form submit button.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
@Immutable
public final class UISubmit implements UIElement {

    /**
     * Text that goes on the button
     */
    private final String value;

    /**
     * Possibly null icon to use on the button
     */
    private final URI icon;

    private UISubmit(String value, URI icon) {
        this.value = Check.notNull(value);
        this.icon = icon;
    }
    
    public static UISubmit value(String value) {
        return new UISubmit(value,null);
    }

    public static UISubmit valueIcon(String value, URI icon) {
        return new UISubmit(value,icon);
    }

    @Override
    public String toString() {
        if (icon==null) {
            return Replace.bracketQuote("<input type=[submit] value=[" + value + "] />");
        }
        return Replace.bracketQuote("<input type=[image] src=[" + icon + "] value=[" + value + "] />");
    }
}

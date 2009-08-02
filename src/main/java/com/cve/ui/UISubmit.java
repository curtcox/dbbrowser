package com.cve.ui;

import com.cve.util.Check;

/**
 * Like a HTML form submit button.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UISubmit implements UIElement {

    private final String value;

    private UISubmit(String value) {
        this.value = Check.notNull(value);
    }
    
    public static UISubmit value(String value) {
        return new UISubmit(value);
    }

    @Override
    public String toString() {
        return "<input type=\"submit\" value=\"" + value + "\" />";
    }
}

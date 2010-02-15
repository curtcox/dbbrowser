package com.cve.ui;

import com.cve.util.Check;

/**
 * Like a HTML form text.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UILabel implements UIElement {

    private final String value;

    private UILabel(String value) {
        this.value = Check.notNull(value);
    }
    
    public static UILabel of(String value) {
        return new UILabel(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

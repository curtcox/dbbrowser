package com.cve.ui;

import com.cve.util.Check;
import com.cve.util.Replace;

/**
 * Like a HTML form text field.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UIText implements UIElement {

    private final String name;
    private final String value;

    private UIText(String name, String value) {
        this.name  = Check.notNull(name);
        this.value = Check.notNull(value);
    }

    /**
     * Return a text box, given it's name in the form and initial value.
     */
    public static UIText nameValue(String name, String value) {
        return new UIText(name,value);
    }

    @Override
    public String toString() {
        return Replace.bracketQuote("<input type=[text] name=["+ name + "] value=[" + value + "] />");
    }
}

package com.cve.ui;

import com.cve.html.CSS;
import com.cve.html.HTML;
import com.cve.util.Check;

/**
 * Like a HTML table detail.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UIDetail {

    /**
     * Either value or element is not null.
     */
    private final UIElement element;

    /**
     * Either value or element is not null.
     */
    private final String value;
    private final CSS css;
    private final int width;

    private static final int UNSET = -1;

    private UIDetail(String value, int width, CSS css) {
        this.value = Check.notNull(value);
        this.element = null;
        this.css   = css;
        this.width = width;
    }

    private UIDetail(UIElement element, int width, CSS css) {
        this.element = Check.notNull(element);
        this.value = null;
        this.css   = css;
        this.width = width;
    }

    public static UIDetail of(UIElement element) {
        return new UIDetail(element,UNSET,null);
    }

    public static UIDetail of(String value) {
        return new UIDetail(value,UNSET,null);
    }

    public static UIDetail of(String value, CSS css) {
        return new UIDetail(value,UNSET,css);
    }

    public static UIDetail of(String value, int width) {
        return new UIDetail(value,width,null);
    }

    @Override
    public String toString() {
        String body = value == null ? element.toString() : value;

        if (width==UNSET && css==null) {
            return HTML.td(body);
        }
        if (width!=UNSET) {
            return HTML.td(body,width);
        }
        if (css==null) {
            return HTML.td(body);
        }
        return HTML.td(body,css);
    }
}

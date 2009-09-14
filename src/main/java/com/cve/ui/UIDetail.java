package com.cve.ui;

import com.cve.html.CSS;
import com.cve.html.HTML;
import com.cve.util.Check;
import javax.annotation.concurrent.Immutable;

/**
 * Like a HTML table detail.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
@Immutable
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
    private final int height;

    private static final int UNSET = -1;

    private UIDetail(String value, int width, CSS css, int height) {
        this.value = Check.notNull(value);
        this.element = null;
        this.css   = css;
        this.width = width;
        this.height = height;
    }

    private UIDetail(UIElement element, int width, CSS css, int height) {
        this.element = Check.notNull(element);
        this.value = null;
        this.css   = css;
        this.width = width;
        this.height = height;
    }

    public static UIDetail of(UIElement element) {
        return new UIDetail(element,UNSET,null,UNSET);
    }

    public static UIDetail of(String value) {
        return new UIDetail(value,UNSET,null,UNSET);
    }

    public static UIDetail of(String value, CSS css) {
        return new UIDetail(value,UNSET,css,UNSET);
    }

    public static UIDetail valueCssWidthHeight(String value, CSS css, int width, int height) {
        return new UIDetail(value,width,css,height);
    }

    public static UIDetail of(String value, int width) {
        return new UIDetail(value,width,null,UNSET);
    }

    @Override
    public String toString() {
        String body = value == null ? element.toString() : value;
        if (height!=UNSET) {
            return HTML.td(body,css,width,height);
        }

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

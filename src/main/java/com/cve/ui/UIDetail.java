package com.cve.ui;

import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import static com.cve.util.Check.notNull;
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

    private final HTMLTags tags;

    public final Log log;

    private static final int UNSET = -1;

    private UIDetail(String value, int width, CSS css, int height, Log log) {
        this.value = notNull(value);
        this.element = null;
        this.css   = css;
        this.width = width;
        this.height = height;
        this.log = notNull(log);
        tags = HTMLTags.of(log);
    }

    private UIDetail(UIElement element, int width, CSS css, int height, Log log) {
        this.element = notNull(element);
        this.value = null;
        this.css   = css;
        this.width = width;
        this.height = height;
        this.log = notNull(log);
        tags = HTMLTags.of(log);
    }

    public static UIDetail of(UIElement element, Log log) {
        return new UIDetail(element,UNSET,null,UNSET,log);
    }

    public static UIDetail of(String value, Log log) {
        return new UIDetail(value,UNSET,null,UNSET,log);
    }

    public static UIDetail of(String value, CSS css, Log log) {
        return new UIDetail(value,UNSET,css,UNSET,log);
    }

    public static UIDetail valueCssWidthHeight(String value, CSS css, int width, int height, Log log) {
        return new UIDetail(value,width,css,height,log);
    }

    public static UIDetail of(String value, int width, Log log) {
        return new UIDetail(value,width,null,UNSET,log);
    }

    @Override
    public String toString() {
        String body = value == null ? element.toString() : value;
        if (height!=UNSET) {
            return tags.td(body,css,width,height);
        }

        if (width==UNSET && css==null) {
            return tags.td(body);
        }
        if (width!=UNSET) {
            return tags.td(body,width);
        }
        if (css==null) {
            return tags.td(body);
        }
        return tags.td(body,css);
    }
}

package com.cve.ui;

import com.cve.html.CSS;
import com.cve.log.Log;
import com.cve.log.Logs;
import static com.cve.util.Check.notNull;
import javax.annotation.concurrent.Immutable;

/**
 * Like a HTML table detail.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
@Immutable
public final class UITableDetail implements UITableCell {

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

    public final Log log = Logs.of();

    private static final int UNSET = -1;

    private UITableDetail(String value, int width, CSS css, int height) {
        this.value = notNull(value);
        this.element = null;
        this.css   = css;
        this.width = width;
        this.height = height;
        
        tags = HTMLTags.of();
    }

    private UITableDetail(UIElement element, int width, CSS css, int height) {
        this.element = notNull(element);
        this.value = null;
        this.css   = css;
        this.width = width;
        this.height = height;
        
        tags = HTMLTags.of();
    }

    public static UITableDetail of(UIElement element) {
        return new UITableDetail(element,UNSET,null,UNSET);
    }

    public static UITableDetail of(String value) {
        return new UITableDetail(value,UNSET,null,UNSET);
    }

    public static UITableDetail of(String value, CSS css) {
        return new UITableDetail(value,UNSET,css,UNSET);
    }

    public static UITableDetail valueCssWidthHeight(String value, CSS css, int width, int height) {
        return new UITableDetail(value,width,css,height);
    }

    public static UITableDetail of(String value, int width) {
        return new UITableDetail(value,width,null,UNSET);
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

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
public final class UITableHeader implements UITableCell {

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

    private UITableHeader(String value, int width, CSS css, int height) {
        this.value = notNull(value);
        this.element = null;
        this.css   = css;
        this.width = width;
        this.height = height;
        
        tags = HTMLTags.of();
    }

    private UITableHeader(UIElement element, int width, CSS css, int height) {
        this.element = notNull(element);
        this.value = null;
        this.css   = css;
        this.width = width;
        this.height = height;
        
        tags = HTMLTags.of();
    }

    public static UITableHeader of(UIElement element) {
        return new UITableHeader(element,UNSET,null,UNSET);
    }

    public static UITableHeader of(String value) {
        return new UITableHeader(value,UNSET,null,UNSET);
    }

    public static UITableHeader of(String value, CSS css) {
        return new UITableHeader(value,UNSET,css,UNSET);
    }

    public static UITableHeader valueCssWidthHeight(String value, CSS css, int width, int height) {
        return new UITableHeader(value,width,css,height);
    }

    public static UITableHeader of(String value, int width) {
        return new UITableHeader(value,width,null,UNSET);
    }

    @Override
    public String toString() {
        String body = value == null ? element.toString() : value;
        if (height!=UNSET) {
            return tags.th(body,css,width,height);
        }

        if (width==UNSET && css==null) {
            return tags.th(body);
        }
        if (width!=UNSET) {
            return tags.th(body,width);
        }
        if (css==null) {
            return tags.th(body);
        }
        return tags.th(body,css);
    }
}

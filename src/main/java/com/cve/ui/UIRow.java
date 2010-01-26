package com.cve.ui;

import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Like a HTML table row.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
@Immutable
public final class UIRow {

    private final HTMLTags tags;
    private final ImmutableList<UIDetail> details;
    private final CSS css;
    private final Log log;

    private UIRow(List<UIDetail> details, CSS css, Log log) {
        this.details = ImmutableList.copyOf(details);
        this.css     = css;
        this.log = notNull(log);
        tags = HTMLTags.of(log);
    }

    public static UIRow of(List<UIDetail> details, CSS css, Log log) {
        return new UIRow(details,css,log);
    }

    public static UIRow of(List<UIDetail> details, Log log) {
        return new UIRow(details,null,log);
    }

    public static UIRow of(Log log, UIDetail... details) {
        return new UIRow(ImmutableList.of(details),null,log);
    }

    public static UIRow of(Log log, UIElement... elements) {
        List<UIDetail> details = Lists.newArrayList();
        for (UIElement element : elements) {
            details.add(UIDetail.of(element,log));
        }
        return new UIRow(ImmutableList.copyOf(details),null,log);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIDetail detail : details) {
            out.append(detail.toString());
        }
        if (css==null) {
            return tags.tr(out.toString());
        }
        return tags.tr(out.toString(), css);
    }
}

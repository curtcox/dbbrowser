package com.cve.ui;

import com.cve.html.CSS;
import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;

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
    private final Log log = Logs.of();

    private UIRow(List<UIDetail> details, CSS css) {
        this.details = ImmutableList.copyOf(details);
        this.css     = css;
        
        tags = HTMLTags.of();
    }

    public static UIRow of(List<UIDetail> details, CSS css) {
        return new UIRow(details,css);
    }

    public static UIRow of(List<UIDetail> details) {
        return new UIRow(details,null);
    }

    public static UIRow of(UIDetail... details) {
        return new UIRow(ImmutableList.of(details),null);
    }

    public static UIRow of(UIElement... elements) {
        List<UIDetail> details = Lists.newArrayList();
        for (UIElement element : elements) {
            details.add(UIDetail.of(element));
        }
        return new UIRow(ImmutableList.copyOf(details),null);
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

package com.cve.ui;

import com.cve.html.CSS;
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
public final class UITableRow {

    public final ImmutableList<UITableCell> details;
    public final CSS css;

    private final Log log = Logs.of();
    private final HTMLTags tags;

    private UITableRow(List<UITableCell> details, CSS css) {
        this.details = ImmutableList.copyOf(details);
        this.css     = css;
        
        tags = HTMLTags.of();
    }

    public static UITableRow of(List<UITableCell> details, CSS css) {
        return new UITableRow(details,css);
    }

    public static UITableRow of(List<UITableCell> details) {
        return new UITableRow(details,null);
    }

    public static UITableRow of(UITableCell... details) {
        return new UITableRow(ImmutableList.of(details),null);
    }

    public static UITableRow of(CSS css, UITableCell... details) {
        return new UITableRow(ImmutableList.of(details),css);
    }

    public static UITableRow of(UIElement... elements) {
        List<UITableCell> details = Lists.newArrayList();
        for (UIElement element : elements) {
            details.add(UITableDetail.of(element));
        }
        return new UITableRow(ImmutableList.copyOf(details),null);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UITableCell detail : details) {
            out.append(detail.toString());
        }
        if (css==null) {
            return tags.tr(out.toString());
        }
        return tags.tr(out.toString(), css);
    }
}

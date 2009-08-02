package com.cve.ui;

import com.cve.html.CSS;
import com.cve.html.HTML;
import com.google.common.collect.ImmutableList;
import java.util.List;

/**
 * Like a HTML table row.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UIRow {

    private final ImmutableList<UIDetail> details;
    private final CSS css;

    private UIRow(List<UIDetail> details, CSS css) {
        this.details = ImmutableList.copyOf(details);
        this.css     = css;
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

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIDetail detail : details) {
            out.append(detail.toString());
        }
        if (css==null) {
            return HTML.tr(out.toString());
        }
        return HTML.tr(out.toString(), css);
    }
}

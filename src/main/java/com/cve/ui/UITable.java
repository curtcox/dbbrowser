
package com.cve.ui;

import com.cve.html.HTML;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Like a HTML table.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 * @author curt
 */
public final class UITable implements UIElement {

    private final ImmutableList<UIRow> rows;

    private UITable(List<UIRow> rows) {
        ImmutableList<UIRow> copy = ImmutableList.copyOf(Check.notNull(rows));
        this.rows = copy;
    }

    public static UITable of() {
        ImmutableList<UIRow> rows = ImmutableList.of();
        return new UITable(rows);
    }

    public static UITable of(List<UIRow> rows) {
        return new UITable(ImmutableList.copyOf(rows));
    }

    public static UITable of(UIRow... rows) {
        return new UITable(ImmutableList.of(rows));
    }

    public UITable with(UIRow row) {
        List<UIRow> list = Lists.newArrayList();
        list.addAll(rows);
        list.add(row);
        return new UITable(ImmutableList.copyOf(list));
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIRow row : rows) {
            out.append(row.toString());
        }
        return HTML.borderTable(out.toString());
    }
}

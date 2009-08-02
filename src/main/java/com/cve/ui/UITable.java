
package com.cve.ui;

import com.cve.html.HTML;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.util.List;

/**
 * Like a HTML table.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 * @author curt
 */
public final class UITable implements UIElement {

    private final ImmutableList<UIRow> rows;

    private UITable(ImmutableList<UIRow> rows) {
        this.rows = Check.notNull(rows);
    }

    public static UITable of(List<UIRow> rows) {
        return new UITable(ImmutableList.copyOf(rows));
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIRow row : rows) {
            out.append(row.toString());
        }
        return HTML.table(out.toString());
    }
}

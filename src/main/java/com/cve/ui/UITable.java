
package com.cve.ui;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * Like a HTML table.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 *
 * @see UITableBuilder
 * @author curt
 */
@Immutable
public final class UITable implements UIElement {

    private final Log log = Logs.of();

    private final HTMLTags tags;

    private final ImmutableList<UITableRow> rows;

    private UITable(List<UITableRow> rows) {
        ImmutableList<UITableRow> copy = ImmutableList.copyOf(Check.notNull(rows));
        this.rows = copy;
        
        tags = HTMLTags.of();
    }

    public static UITable of() {
        ImmutableList<UITableRow> rows = ImmutableList.of();
        return new UITable(rows);
    }

    public static UITable of(List<UITableRow> rows) {
        return new UITable(ImmutableList.copyOf(rows));
    }

    public static UITable of(UITableRow... rows) {
        return new UITable(ImmutableList.of(rows));
    }

    public UITable with(UITableRow row) {
        List<UITableRow> list = Lists.newArrayList();
        list.addAll(rows);
        list.add(row);
        return new UITable(ImmutableList.copyOf(list));
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UITableRow row : rows) {
            out.append(row.toString());
        }
        return tags.borderTable(out.toString());
    }
}

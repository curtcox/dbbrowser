
package com.cve.ui;

import com.cve.html.HTMLTags;
import com.cve.log.Log;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * Like a HTML table.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 * @author curt
 */
@Immutable
public final class UITable implements UIElement {

    private final Log log;

    private final HTMLTags tags;

    private final ImmutableList<UIRow> rows;

    private UITable(List<UIRow> rows, Log log) {
        ImmutableList<UIRow> copy = ImmutableList.copyOf(Check.notNull(rows));
        this.rows = copy;
        this.log = notNull(log);
        tags = HTMLTags.of(log);
    }

    public static UITable of(Log log) {
        ImmutableList<UIRow> rows = ImmutableList.of();
        return new UITable(rows,log);
    }

    public static UITable of(List<UIRow> rows, Log log) {
        return new UITable(ImmutableList.copyOf(rows),log);
    }

    public static UITable of(Log log, UIRow... rows) {
        return new UITable(ImmutableList.of(rows),log);
    }

    public UITable with(UIRow row) {
        List<UIRow> list = Lists.newArrayList();
        list.addAll(rows);
        list.add(row);
        return new UITable(ImmutableList.copyOf(list),log);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (UIRow row : rows) {
            out.append(row.toString());
        }
        return tags.borderTable(out.toString());
    }
}

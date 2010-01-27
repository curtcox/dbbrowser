package com.cve.ui;

import com.cve.log.Log;
import com.google.common.collect.Lists;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * For building UITables.
 * @author Curt
 */
public final class UITableBuilder {

    final Log log;

    private List<UIRow> rows = Lists.newArrayList();

    private UITableBuilder(Log log) {
        this.log = notNull(log);
    }

    public static UITableBuilder of(Log log) {
        return new UITableBuilder(log);
    }

    public void add(UIRow row) {
        rows.add(row);
    }

    public UITable build() {
        return UITable.of(rows,log);
    }
}

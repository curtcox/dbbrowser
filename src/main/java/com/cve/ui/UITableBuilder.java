package com.cve.ui;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * For building UITables.
 * @author Curt
 */
public final class UITableBuilder {

    private List<UIRow> rows = Lists.newArrayList();

    public UITableBuilder() {}

    public void add(UIRow row) {
        rows.add(row);
    }

    public UITable build() {
        return UITable.of(rows);
    }
}

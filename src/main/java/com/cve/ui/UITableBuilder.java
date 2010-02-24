package com.cve.ui;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * For building UITables.
 * @author Curt
 */
public final class UITableBuilder {

    final Log log = Logs.of();

    private List<UITableRow> rows = Lists.newArrayList();

    /**
     * Use the factory.
     */
    private UITableBuilder() {}

    public static UITableBuilder of() {
        return new UITableBuilder();
    }

    public void add(UITableRow row) {
        rows.add(row);
    }

    public void addRow(UIElement... elements) {
        rows.add(UITableRow.of(elements));
    }

    public UITable build() {
        return UITable.of(rows);
    }
}

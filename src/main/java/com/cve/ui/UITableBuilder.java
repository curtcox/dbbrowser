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

    private List<UIRow> rows = Lists.newArrayList();

    private UITableBuilder() {
        
    }

    public static UITableBuilder of() {
        return new UITableBuilder();
    }

    public void add(UIRow row) {
        rows.add(row);
    }

    public UITable build() {
        return UITable.of(rows);
    }
}

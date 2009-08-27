package com.cve.web.alt;

import com.cve.db.Value;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * For building immutable CSVModels.
 * @author curt
 */
public final class CSVModelBuilder {

    /**
     * The row we are currently building.
     */
    private List<String> currentRow = Lists.newArrayList();

    /**
     * All of the rows.
     */
    private final List<ImmutableList<String>> values = Lists.newArrayList();

    /**
     * Add a value to the current row.
     */
    void add(Value value) {
        currentRow.add("" + value.value);
    }

    /**
     * End the current row and start a new one.
     */
    void next() {
        values.add(ImmutableList.copyOf(currentRow));
        currentRow = Lists.newArrayList();
    }

    CSVModel build() {
        return new CSVModel(values);
    }

}

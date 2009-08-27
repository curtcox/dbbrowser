package com.cve.web.alt;

import com.cve.util.Check;
import com.cve.web.Model;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * A structured abstraction of CSV
 * @author Curt
 */
@Immutable
final class CSVModel implements Model {

    public final ImmutableList<ImmutableList<String>> values;

    CSVModel(List<ImmutableList<String>> values) {
        this.values = ImmutableList.copyOf(Check.notNull(values));
    }
}

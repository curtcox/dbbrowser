package com.cve.io.db.select;

import com.cve.model.db.Select;
import com.cve.model.db.SelectContext;
import com.cve.model.db.SelectResults;
/**
 * Runs {@link Select}S against database connections to produce {@link SelectResults}.
 */
interface SelectRunner {

    SelectResults run(SelectContext context);
}

package com.cve.db.select;

import com.cve.db.Select;
import com.cve.db.SelectContext;
import com.cve.db.SelectResults;
/**
 * Runs {@link Select}S against database connections to produce {@link SelectResults}.
 */
interface SelectRunner {

    SelectResults run(SelectContext context);
}

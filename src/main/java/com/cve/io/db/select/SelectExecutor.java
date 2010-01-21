package com.cve.io.db.select;

import com.cve.log.Log;
import com.cve.model.db.SelectContext;
import com.cve.model.db.SelectResults;
import com.cve.util.Stopwatch;

/**
 * How everyone outside of this package executes selects.
 * @author curt
 */
public final class SelectExecutor {

    final Log log;

    public SelectResults run(SelectContext context) {
        log.notNullArgs(context);
        Stopwatch watch = Stopwatch.start(context.select);
        SelectRunner runner = new SimpleSelectRunner();
        SelectResults results = runner.run(context);
        watch.stop();
        return results;
    }

}

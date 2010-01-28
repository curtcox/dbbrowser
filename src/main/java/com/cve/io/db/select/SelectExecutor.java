package com.cve.io.db.select;

import com.cve.log.Log;
import com.cve.model.db.SelectContext;
import com.cve.model.db.SelectResults;
import com.cve.util.Stopwatch;
import static com.cve.util.Check.notNull;

/**
 * How everyone outside of this package executes selects.
 * @author curt
 */
public final class SelectExecutor {

    final Log log;

    private SelectExecutor(Log log) {
        this.log = notNull(log);
    }

    public static SelectExecutor of(Log log) {
        return new SelectExecutor(log);
    }
    
    public SelectResults run(SelectContext context) {
        log.args(context);
        Stopwatch watch = Stopwatch.start(context.select);
        SelectRunner runner = SimpleSelectRunner.of(log);
        SelectResults results = runner.run(context);
        watch.stop();
        return results;
    }

}

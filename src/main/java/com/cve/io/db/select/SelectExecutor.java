package com.cve.io.db.select;

import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.model.db.SelectContext;
import com.cve.model.db.SelectResults;
import com.cve.util.Stopwatch;

/**
 * How everyone outside of this package executes selects.
 * @author curt
 */
public final class SelectExecutor {

    final Log log = Logs.of();

    private SelectExecutor() {
        
    }

    public static SelectExecutor of() {
        return new SelectExecutor();
    }
    
    public SelectResults run(SelectContext context) {
        log.args(context);
        Stopwatch watch = Stopwatch.start(context.select);
        SelectRunner runner = SimpleSelectRunner.of();
        SelectResults results = runner.run(context);
        watch.stop();
        return results;
    }

}

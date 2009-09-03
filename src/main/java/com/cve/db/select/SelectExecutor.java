package com.cve.db.select;

import com.cve.db.SelectContext;
import com.cve.db.SelectResults;
import com.cve.util.Stopwatch;
import static com.cve.log.Log.args;

/**
 * How everyone outside of this package executes selects.
 * @author curt
 */
public final class SelectExecutor {

    public static SelectResults run(SelectContext context) {
        args(context);
        Stopwatch watch = Stopwatch.start(context.select);
        SelectRunner runner = new SimpleSelectRunner();
        SelectResults results = runner.run(context);
        watch.stop();
        return results;
    }

}

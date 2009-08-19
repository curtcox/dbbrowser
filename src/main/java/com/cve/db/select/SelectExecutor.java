package com.cve.db.select;

import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.Server;
import com.cve.db.dbio.DBConnection;
import com.cve.util.Stopwatch;

/**
 * How everyone outside of this package executes selects.
 * @author curt
 */
public final class SelectExecutor {

    public static SelectResults run(Select select, Server server, DBConnection connection, Hints hints) {
        Stopwatch watch = Stopwatch.start(select);
        SelectRunner runner = new SimpleSelectRunner();
        SelectResults results = runner.run(select, server, connection, hints);
        watch.stop();
        return results;
    }

}

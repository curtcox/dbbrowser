package com.cve.db.select;

import com.cve.db.dbio.DBConnection;
import com.cve.db.Hints;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.SelectResults;
/**
 * Runs {@link Select}S against database connections to produce {@link SelectResults}.
 */
interface SelectRunner {

    SelectResults run(Select select, Server server, DBConnection connection, Hints hints);
}

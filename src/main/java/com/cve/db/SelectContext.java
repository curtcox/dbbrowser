package com.cve.db;

import com.cve.db.dbio.DBConnection;
import com.cve.util.Check;
import com.cve.web.Search;
import javax.annotation.concurrent.Immutable;

/**
 * A select statement, plus the context it is run in.
 * @author curt
 */
@Immutable
public final class SelectContext {

    public final Select select;

    public final Search search;

    public final Server server;

    public final DBConnection connection;

    public final Hints hints;

    private SelectContext(Select select, Search search, Server server, DBConnection connection, Hints hints) {
        this.select = Check.notNull(select);
        this.search = Check.notNull(search);
        this.server = Check.notNull(server);
        this.connection = Check.notNull(connection);
        this.hints = Check.notNull(hints);
        
    }

    public static SelectContext of(Select select, Search search, Server server, DBConnection connection, Hints hints) {
        return new SelectContext(select,search,server,connection,hints);
    }
}

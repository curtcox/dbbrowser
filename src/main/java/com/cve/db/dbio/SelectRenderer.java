package com.cve.db.dbio;

import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.web.Search;
/**
 * Renders a {@link Select} as {@link SQL}.
 * This is how a select is turned into SQL.
 * @author curt
 */
interface SelectRenderer {

    /**
     * Render the given select as SQL.
     */
    SQL render(Select select, Search search);

    /**
     * Same as render, but return the SQL for a count of the rows, rather
     * than the rows themselves.
     */
    SQL renderCount(Select select, Search search);

}

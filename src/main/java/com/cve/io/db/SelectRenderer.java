package com.cve.io.db;

import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.web.core.Search;
/**
 * Renders a {@link Select} as {@link SQL}.
 * This is how a select is turned into SQL.
 * @author curt
 */
public interface SelectRenderer {

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

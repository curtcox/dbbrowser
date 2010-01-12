package com.cve.web.db.databases;

import com.cve.model.db.Database;
import com.cve.model.db.SelectResults;
import com.cve.web.*;

import com.google.common.collect.ImmutableList;

import java.util.List;
import static com.cve.util.Check.notNull;
import static com.cve.log.Log.args;

/**
 * The results of searching the the contents of tables in a database.
 */
public final class DatabaseContentsSearchPage implements Model {

    final Search search;

    final Database database;

    /**
     * The tables on the page
     */
    final ImmutableList<SelectResults> resultsList;

    private DatabaseContentsSearchPage(Search search, Database database, List<SelectResults> resultsList) {
        args(search,database,resultsList);
        this.search      = notNull(search);
        this.database    = notNull(database);
        this.resultsList = ImmutableList.copyOf(notNull(resultsList));
        for (SelectResults results : resultsList) {
            if (!search.equals(results.search)) {
                String message = search + " != " + results.search;
                throw new IllegalArgumentException(message);
            }
        }
    }

    public static DatabaseContentsSearchPage of(Search search, Database database, List<SelectResults> resultsList) {
        return new DatabaseContentsSearchPage(search,database,resultsList);
    }
}

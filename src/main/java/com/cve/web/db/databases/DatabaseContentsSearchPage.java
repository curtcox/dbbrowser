package com.cve.web.db.databases;

import com.cve.log.Log;
import com.cve.model.db.Database;
import com.cve.model.db.SelectResults;
import com.cve.web.*;

import com.google.common.collect.ImmutableList;

import java.util.List;
import static com.cve.util.Check.notNull;

/**
 * The results of searching the the contents of tables in a database.
 */
public final class DatabaseContentsSearchPage implements Model {

    final Search search;

    final Database database;

    final Log log;

    /**
     * The tables on the page
     */
    final ImmutableList<SelectResults> resultsList;

    private DatabaseContentsSearchPage(Search search, Database database, List<SelectResults> resultsList, Log log) {
        log.notNullArgs(search,database,resultsList);
        this.log = notNull(log);
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

    public static DatabaseContentsSearchPage of(Search search, Database database, List<SelectResults> resultsList, Log log) {
        return new DatabaseContentsSearchPage(search,database,resultsList,log);
    }
}

package com.cve.web.db;

import com.cve.db.DBColumn;
import com.cve.db.Filter;
import com.cve.db.Join;
import com.cve.db.Limit;
import com.cve.db.Select;
import com.cve.db.DBServer;
import com.cve.db.DBTable;
import com.cve.db.Order;
import com.cve.db.dbio.DBMetaData;
import com.cve.util.Check;
import com.cve.util.URIs;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import static com.cve.util.Check.notNull;
/**
 * Actions that are used to build a select statement.
 * From a web application viewpoint, these can be considered actions.
 * From a REST viewpoint, they are operators that operate on one value in
 * URI space to produce another.
 * <p>
 * The heart of the application is the page that iteratively builds a select.
 * Each link, essentially represnts an action on a URL that turns it into
 * another URL.
 * <p>
 * At some point, this could be used to generate Javascript that would
 * reduce round-trips and therefore page latency.
 */
public enum SelectBuilderAction {

    /**
     * Modify the limit to page forward in the results.
     */
    NEXT("next") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            int   pages = Integer.parseInt(query);
            Limit limit = select.limit;
            select = select.with(limit.next(pages));
            return select;
        }
    },

    /**
     * Modify the limit to page back in the results.
     */
    BACK("back") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            int   pages = Integer.parseInt(query);
            Limit limit = select.limit;
            select = select.with(limit.back(pages));
            return select;
        }
    },

    /**
     * Make the maximum number of rows returned bigger.
     */
    BIGGER("bigger") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            int   factor = Integer.parseInt(query);
            Limit limit = select.limit;
            select = select.with(limit.bigger(factor));
            return select;
        }
    },

    /**
     * Make the maximum number of rows returned smaller.
     */
    SMALLER("smaller") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            int   factor = Integer.parseInt(query);
            Limit limit = select.limit;
            select = select.with(limit.smaller(factor));
            return select;
        }
    },

    /**
     * Remove the given column from the select.
     */
    HIDE("hide") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            return select.without(DBColumn.parse(server,select.tables,query));
        }
    },

    /**
     * Add the given column to the select.
     */
    SHOW("show") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
             return select.with(DBColumn.parse(server,select.tables,query));
        }
    },

    /**
     * Add the given filter to the select.
     */
    FILTER("filter") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            return select.with(Filter.parse(server,select.tables,query));
        }
    },

    /**
     * Add the given filter to the select.
     */
    ORDER("order") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) {
            return select.with(Order.parse(server,select.tables,query));
        }
    },

    /**
     * Add the given join to the select.
     */
    JOIN("join") {
        @Override
        public Select goDo(Select select, DBServer server, DBMetaData.Factory db, String query) throws SQLException {
            Join join = Join.parse(server,select.tables,query);
            select = select.with(join);
            DBTable table = join.dest.table;
            DBMetaData meta = db.of(table.database.server);
            for (DBColumn column : meta.getColumnsFor(table).value) {
                select = select.with(column);
            }
            return select;
        }
    };

    /**
     * The string that gets put in URLs.
     */
    private final String action;

    SelectBuilderAction(String action) {
        this.action = action;
    }

    /**
     * Use the given args to create a URI for this action.
     */
    public URI withArgs(String args) {
        notNull(args);
        return URIs.of(action + "?" + args);
    }

    /**
     * Given a select and a server, use this action and the given args to
     * produce another select statement.
     * In loving memory of Harvey Korman.
     */
    public abstract Select goDo(Select select, DBServer server, DBMetaData.Factory db, String args) throws SQLException;

    /**
     * Find the relevant action and go do it.
     */
    public static Select doAction(
        String actionString, Select select, DBServer server,
        DBMetaData.Factory db, String query)
        throws SQLException
    {
        Check.notNull(actionString);
        Check.notNull(select);
        Check.notNull(server);
        Check.notNull(query);
        SelectBuilderAction action = findAction(actionString);
        return action.goDo(select, server, db, query);
    }

    /**
     * Find the action with the given name.
     */
    static SelectBuilderAction findAction(String actionString) {
        for (SelectBuilderAction action : values()) {
            if (action.action.equalsIgnoreCase(actionString)) {
                return action;
            }
        }
        String message = "Encounterd " + actionString + ", but expected " + Arrays.asList(values());
        throw new IllegalArgumentException(message);
    }




}

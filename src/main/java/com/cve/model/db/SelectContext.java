package com.cve.model.db;

import com.cve.io.db.DBConnection;
import com.cve.util.Check;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * A select statement, plus the context it is run in.
 * @author curt
 */
@Immutable
public final class SelectContext {

    public final Select select;

    public final Search search;

    public final DBServer server;

    public final Hints hints;

    public final DBConnection connection;

    private SelectContext(Select select, Search search, DBServer server, DBConnection connection, Hints hints) {
        this.select = Check.notNull(select);
        this.search = Check.notNull(search);
        this.server = Check.notNull(server);
        this.connection = Check.notNull(connection);
        this.hints = Check.notNull(hints);
        
    }

    public static SelectContext of(Select select, Search search, DBServer server, DBConnection connection, Hints hints) {
        return new SelectContext(select,search,server,connection,hints);
    }

    /**
     * Return a similar context that has been verified against the database
     */
    public SelectContext verified() throws SQLException {
        Select newSelect = select.with(keyed(select.columns));
        return of(newSelect,search,server,connection,hints);
    }

    /**
     * Reurn a list of the same columns, but with the proper key information.
     */
    ImmutableList<DBColumn> keyed(ImmutableList<DBColumn> columns) throws SQLException {
        List<DBColumn> list = Lists.newArrayList();
        for (DBColumn column : columns) {
            DBColumn keyed = keyed(column);
            list.add(keyed);
        }
        return ImmutableList.copyOf(list);
    }

    /**
     * Return an equivalent column, with its keyness set by the database.
     */
    DBColumn keyed(DBColumn target) throws SQLException {
        DBTable table = target.table;
        for (DBColumn column : connection.getMetaData().getColumnsFor(table).value) {
            if (column.name.equals(target.name)) {
                return column;
            }
        }
        throw new IllegalArgumentException("" + target);
    }
}

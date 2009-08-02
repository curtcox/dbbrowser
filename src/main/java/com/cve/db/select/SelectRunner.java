package com.cve.db.select;

import com.cve.db.AggregateFunction;
import com.cve.db.dbio.DBConnection;
import com.cve.db.Cell;
import com.cve.db.DBColumn;
import com.cve.db.DBResultSet;
import com.cve.db.Database;
import com.cve.db.Hints;
import com.cve.db.Limit;
import com.cve.db.DBRow;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.Server;
import com.cve.db.SelectResults;
import com.cve.db.DBTable;
import com.cve.db.Value;
import com.cve.db.dbio.DBDriver;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static com.cve.util.Check.notNull;
/**
 * Runs {@link Select}S against database connections to produce {@link SelectResults}.
 */
public final class SelectRunner {

    public static SelectResults run(Select select, Server server, DBConnection connection, Hints hints) {
        try {
            return tryRun(select,server,connection,hints);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static SelectResults tryRun(Select select, Server server, DBConnection connection, Hints hints) throws SQLException {
        notNull(select);
        notNull(server);
        notNull(connection);
        notNull(hints);
        DBDriver driver = connection.getInfo().getDriver();
        SQL         sql = SelectRenderers.render(select,driver);
        int  count = determineRowCount(select,server,connection);
        try {
            ResultSet results = connection.exec(sql);
            try {
                SelectResults.Type  type = determineResultsType(select);
                ResultsAndMore immutable = transform(select,results);
                return SelectResults.typeSelectResultsHintsCountMore(type,select,immutable.resultSet,hints,count,immutable.more);
            } finally {
                results.close();
            }
        } catch (SQLException e) {
            throw new SQLException(sql.toString(),e);
        }
    }

    /**
     * Return the number of rows that would be returned if no limit was used.
     */
    static int determineRowCount(Select select, Server server, DBConnection connection) throws SQLException {
        DBDriver driver = connection.getInfo().getDriver();
        SQL sql = SelectRenderers.render(select.count().with(Limit.DEFAULT),driver);
        try {
            ResultSet results = connection.exec(sql);
            try {
                results.next();
                return results.getInt(1);
            } finally {
                results.close();
            }
        } catch (SQLException e) {
            throw new SQLException(sql.toString(),e);
        }
    }

    /**
     * Is the given select normal data or a column value distribution query?
     */
    static SelectResults.Type determineResultsType(Select select) {
        if (select.getGroups().size()!=1) {
            return SelectResults.Type.NORMAL_DATA;
        }
        if (select.getColumns().size()!=2) {
            return SelectResults.Type.NORMAL_DATA;
        }
        return SelectResults.Type.COLUMN_VALUE_DISTRIBUTION;
    }

    /**
     * Transform a select plus java.sql.ResultSet into a
     * dbmodel.DBResultSet.
     */
    static ResultsAndMore transform(Select select, ResultSet results) throws SQLException {
        ImmutableList<Database>   databases = select.getDatabases();
        ImmutableList<DBTable>       tables = select.getTables();
        ImmutableList<DBColumn>     columns = select.getColumns();
        List<DBRow>                    rows = Lists.newArrayList();
        Map<Cell,Value>              values = Maps.newHashMap();
        ResultSetMetaData              meta = results.getMetaData();
        ImmutableList<AggregateFunction> functions = select.getFunctions();
        int cols = meta.getColumnCount();
        int    r = 0;
        Limit limit = select.getLimit();
        while (results.next() && r<(limit.getLimit() - 1)) {
            DBRow row = DBRow.number(r);
            rows.add(row);
            r++;
            for (int c=1; c<=cols; c++) {
                Object v = getObject(results,c);
                Value value = Value.of(v);
                values.put(Cell.at(row, columns.get(c-1),functions.get(c-1)), value);
            }
        }
        boolean more = results.next();
        ImmutableList<DBRow>           fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,Value>   fixedValues = ImmutableMap.copyOf(values);
        return new ResultsAndMore(DBResultSet.of(databases, tables, columns, fixedRows, fixedValues),more);
    }

    private static class ResultsAndMore {
        final com.cve.db.DBResultSet resultSet;
        final boolean more;
        ResultsAndMore(DBResultSet resultSet, boolean more) {
            this.resultSet = resultSet;
            this.more      = more;
        }
    }

    static Object getObject(ResultSet results, int c) throws SQLException {
        try {
            return results.getObject(c);
        } catch (SQLException e) {
            ResultSetMetaData meta = results.getMetaData();
            String        typeName = meta.getColumnTypeName(c);
            String       className = meta.getColumnClassName(c);
            return "Error converting " + typeName + "/" + className;
        }
    }
}

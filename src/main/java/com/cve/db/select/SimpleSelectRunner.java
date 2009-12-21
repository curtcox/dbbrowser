package com.cve.db.select;

import com.cve.db.AggregateFunction;
import com.cve.db.Cell;
import com.cve.db.DBColumn;
import com.cve.db.DBResultSet;
import com.cve.db.Database;
import com.cve.db.Limit;
import com.cve.db.DBRow;
import com.cve.db.SQL;
import com.cve.db.Select;
import com.cve.db.SelectResults;
import com.cve.db.DBTable;
import com.cve.db.Hints;
import com.cve.db.SelectContext;
import com.cve.db.Value;
import com.cve.db.dbio.DBConnection;
import com.cve.db.dbio.driver.DBDriver;
import com.cve.log.Log;
import com.cve.web.Search;
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
import static com.cve.log.Log.args;

/**
 * Runs {@link Select}S against database connections to produce {@link SelectResults}.
 */
final class SimpleSelectRunner implements SelectRunner {

    private static final Log log = Log.of(SimpleSelectRunner.class);

    @Override
    public SelectResults run(SelectContext context) {
        args(context);
        try {
            return tryRun(context.verified());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static SelectResults tryRun(SelectContext context) throws SQLException {
        args(context);
        notNull(context);
        DBConnection connection = context.connection;
        Select select = context.select;
        Search search = context.search;
        DBDriver driver = connection.getInfo().driver;
        SQL         sql = driver.render(select,search);
        int       count = determineRowCount(context);
        try {
            ResultSet results = connection.select(sql);
            try {
                SelectResults.Type  type = determineResultsType(select);
                ResultsAndMore immutable = transform(select,results);
                Hints hints = context.hints;
                return SelectResults.typeSelectSearchResultsHintsCountMore(type,select,search,immutable.resultSet,hints,count,immutable.more);
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
    static int determineRowCount(SelectContext context) throws SQLException {
        DBConnection connection = context.connection;
        DBDriver driver = connection.getInfo().driver;
        Select select = context.select;
        Search search = context.search;
        SQL sql = driver.renderCount(select,search);
        try {
            ResultSet results = connection.select(sql);
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
        args(select);
        if (select.groups.size()!=1) {
            return SelectResults.Type.NORMAL_DATA;
        }
        if (select.columns.size()!=2) {
            return SelectResults.Type.NORMAL_DATA;
        }
        return SelectResults.Type.COLUMN_VALUE_DISTRIBUTION;
    }

    /**
     * Transform a select plus java.sql.ResultSet into a
     * dbmodel.DBResultSet.
     */
    static ResultsAndMore transform(Select select, ResultSet results) throws SQLException {
        args(select,results);
        ImmutableList<Database>   databases = select.databases;
        ImmutableList<DBTable>       tables = select.tables;
        ImmutableList<DBColumn>     columns = select.columns;
        List<DBRow>                    rows = Lists.newArrayList();
        Map<Cell,Value>              values = Maps.newHashMap();
        ResultSetMetaData              meta = results.getMetaData();
        ImmutableList<AggregateFunction> functions = select.functions;
        int cols = meta.getColumnCount();
        int    r = 0;
        Limit limit = select.limit;
        while (results.next() && r<(limit.limit - 1)) {
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

    /**
     * A result set, plus a flag to indicate if more data is available
     */
    private static class ResultsAndMore {
        final com.cve.db.DBResultSet resultSet;
        final boolean more;
        ResultsAndMore(DBResultSet resultSet, boolean more) {
            this.resultSet = resultSet;
            this.more      = more;
        }
    }

    /**
     * Get the object from the speciifed column.
     * Return a string describing the conversion error if one is encountered.
     */
    static Object getObject(ResultSet results, int c) throws SQLException {
        args(results,c);
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

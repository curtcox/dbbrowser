package com.cve.io.db.select;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.Cell;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBResultSet;
import com.cve.model.db.Database;
import com.cve.model.db.DBRow;
import com.cve.model.db.SQL;
import com.cve.model.db.Select;
import com.cve.model.db.SelectResults;
import com.cve.model.db.DBTable;
import com.cve.model.db.Hints;
import com.cve.model.db.SelectContext;
import com.cve.model.db.DBValue;
import com.cve.io.db.DBConnection;
import com.cve.io.db.DBResultSetIO;
import com.cve.io.db.DBResultSetMetaDataIO;
import com.cve.io.db.driver.DBDriver;
import com.cve.log.Log;
import com.cve.web.Search;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static com.cve.util.Check.notNull;

/**
 * Runs {@link Select}S against database connections to produce {@link SelectResults}.
 */
final class SimpleSelectRunner implements SelectRunner {

    private final Log log;

    private SimpleSelectRunner(Log log) {
        this.log = notNull(log);
    }

    public static SimpleSelectRunner of(Log log) {
        return new SimpleSelectRunner(log);
    }
    
    @Override
    public SelectResults run(SelectContext context) {
        log.args(context);
        try {
            return tryRun(context.verified());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    SelectResults tryRun(SelectContext context) throws SQLException {
        log.args(context);
        notNull(context);
        DBConnection connection = context.connection;
        Select select = context.select;
        Search search = context.search;
        DBDriver driver = connection.getInfo().driver;
        SQL         sql = driver.getSelectRenderer().render(select,search);
        long      count = determineRowCount(context);
        DBResultSetIO results = connection.select(sql).value;
        SelectResults.Type  type = determineResultsType(select);
        ResultsAndMore immutable = transform(select,results);
        Hints hints = context.hints;
        return SelectResults.typeSelectSearchResultsHintsCountMore(type,select,search,immutable.resultSet,hints,count,immutable.more,log);
    }

    /**
     * Return the number of rows that would be returned if no limit was used.
     */
    static long determineRowCount(SelectContext context) throws SQLException {
        DBConnection connection = context.connection;
        DBDriver driver = connection.getInfo().driver;
        Select select = context.select;
        Search search = context.search;
        SQL sql = driver.renderCount(select,search);
        DBResultSetIO results = connection.select(sql).value;
        return results.getLong(0,1);
    }

    /**
     * Is the given select normal data or a column value distribution query?
     */
    SelectResults.Type determineResultsType(Select select) {
        log.args(select);
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
    ResultsAndMore transform(Select select, DBResultSetIO results) throws SQLException {
        log.args(select,results);
        ImmutableList<Database>   databases = select.databases;
        ImmutableList<DBTable>       tables = select.tables;
        ImmutableList<DBColumn>     columns = select.columns;
        List<DBRow>                    rows = Lists.newArrayList();
        Map<Cell,DBValue>              values = Maps.newHashMap();
        DBResultSetMetaDataIO          meta = results.meta;
        ImmutableList<AggregateFunction> functions = select.functions;
        int cols = meta.columnCount;
        for (int r=0; r<results.rows.size() - 1; r++) {
            DBRow row = DBRow.number(r);
            for (int c=0; c<cols - 1; c++) {
                Object v = results.rows.get(r).get(c);
                DBValue value = DBValue.of(v);
                values.put(Cell.at(row, columns.get(c),functions.get(c)), value);
            }
            rows.add(row);
        }
        boolean more = results.rows.size() > select.limit.limit;
        ImmutableList<DBRow>           fixedRows = ImmutableList.copyOf(rows);
        ImmutableMap<Cell,DBValue>   fixedValues = ImmutableMap.copyOf(values);
        return new ResultsAndMore(DBResultSet.of(databases, tables, columns, fixedRows, fixedValues,log),more);
    }

    /**
     * A result set, plus a flag to indicate if more data is available
     */
    private static class ResultsAndMore {
        final com.cve.model.db.DBResultSet resultSet;
        final boolean more;
        ResultsAndMore(DBResultSet resultSet, boolean more) {
            this.resultSet = resultSet;
            this.more      = more;
        }
    }

}

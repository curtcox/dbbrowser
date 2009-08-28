package com.cve.db.dbio;

import com.cve.db.AggregateFunction;
import com.cve.db.DBColumn;
import com.cve.db.DBTable;
import com.cve.db.Database;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;

/**
 * Our immutable counterpart to result set meta data
 * @author curt
 */
@Immutable
public final class DBResultSetMetaData {

    public final ImmutableList<Database> databases;

    public final ImmutableList<DBTable> tables;

    public final ImmutableList<DBColumn> columns;

    public final ImmutableList<AggregateFunction> functions;

    private DBResultSetMetaData(ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<AggregateFunction> functions)
    {
        this.databases = Check.notNull(databases);
        this.tables = Check.notNull(tables);
        this.columns = Check.notNull(columns);
        this.functions = Check.notNull(functions);
    }

    static DBResultSetMetaData of(ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<AggregateFunction> functions)
    {
        return new DBResultSetMetaData(databases,tables,columns,functions);
    }

}

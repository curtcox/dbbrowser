package com.cve.io.db;

import com.cve.model.db.AggregateFunction;
import com.cve.model.db.DBColumn;
import com.cve.model.db.DBTable;
import com.cve.model.db.Database;
import com.cve.util.Check;
import com.google.common.collect.ImmutableList;
import java.util.List;
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

    public static final DBResultSetMetaData NULL = Null();
    
    private static DBResultSetMetaData Null() {
        ImmutableList<Database>          databases = ImmutableList.of();
        ImmutableList<DBTable>              tables = ImmutableList.of();
        ImmutableList<DBColumn>            columns = ImmutableList.of();
        ImmutableList<AggregateFunction> functions = ImmutableList.of();
        return new DBResultSetMetaData(databases,tables,columns,functions);
    }

    private DBResultSetMetaData(ImmutableList<Database> databases,
        ImmutableList<DBTable> tables, ImmutableList<DBColumn> columns,
        ImmutableList<AggregateFunction> functions)
    {
        this.databases = Check.notNull(databases);
        this.tables = Check.notNull(tables);
        this.columns = Check.notNull(columns);
        this.functions = Check.notNull(functions);
    }

    public static DBResultSetMetaData of(List<Database> databases,
        List<DBTable> tables, List<DBColumn> columns,
        List<AggregateFunction> functions)
    {
        ImmutableList<Database>          iDatabases = ImmutableList.copyOf(databases);
        ImmutableList<DBTable>              iTables = ImmutableList.copyOf(tables);
        ImmutableList<DBColumn>            iColumns = ImmutableList.copyOf(columns);
        ImmutableList<AggregateFunction> iFunctions = ImmutableList.copyOf(functions);
        return new DBResultSetMetaData(iDatabases,iTables,iColumns,iFunctions);
    }

}

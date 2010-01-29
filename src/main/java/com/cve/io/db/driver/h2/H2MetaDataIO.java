package com.cve.io.db.driver.h2;

import com.cve.io.db.DBConnection;
import com.cve.io.db.DBMetaDataIO;
import com.cve.io.db.DBMetaDataIOLogger;
import com.cve.io.db.DBResultSetIO;
import com.cve.io.db.DefaultDBMetaDataIO;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.stores.CurrentValue;
import com.cve.stores.ManagedFunction;
import com.cve.stores.UnpredictableFunction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.ResultSet;
import java.util.List;
import static com.cve.util.Check.notNull;

/**
 *
 * @author curt
 */
final class H2MetaDataIO extends DefaultDBMetaDataIO {

    final Log log = Logs.of();

    private final ManagedFunction<ColumnSpecifier,DBResultSetIO> columns;

    protected H2MetaDataIO(DBConnection connection, ManagedFunction.Factory managedFunction) {
        super(connection,managedFunction);
        notNull(managedFunction);
        
        columns = notNull(managedFunction.of(new GetColumns(),      ColumnSpecifier.class, DBResultSetIO.class, DBResultSetIO.NULL));
    }

    static DBMetaDataIO of(DBConnection connection, ManagedFunction.Factory managedFunction) {
        DBMetaDataIO io = new H2MetaDataIO(connection,managedFunction);
        io = DBMetaDataIOLogger.of(io);
        return io;
    }

    /**
     * The Javadoc says to use "TABLE_SCHEM" (without the A) in the ResultSet
     * for DBMD.getColumns().  Currently, H2 uses this, instead.
     */
    public static final String TABLE_SCHEMA   = "TABLE_SCHEMA";
    @Override
    public CurrentValue<ImmutableList<ColumnInfo>> getColumns(final ColumnSpecifier specifier) {
        DBResultSetIO results = columns.apply(specifier).value;
        List<ColumnInfo> list = Lists.newArrayList();
        for (int r=0; r<results.rows.size(); r++) {
            String schemaName = results.getString(r,TABLE_SCHEMA);
            String  tableName = results.getString(r,TABLE_NAME);
            String columnName = results.getString(r,COLUMN_NAME);
            int          type = (int) results.getLong(r,DATA_TYPE);
            ColumnInfo column = ColumnInfo.of(schemaName,tableName,columnName,type);
            list.add(column);
        }
        ImmutableList<ColumnInfo> infos = ImmutableList.copyOf(list);
        return CurrentValue.of(infos);
    }

    final class GetColumns implements UnpredictableFunction<ColumnSpecifier, DBResultSetIO> {

        @Override
        public DBResultSetIO apply(ColumnSpecifier specifier) throws Exception {
            ResultSet results = getMetaData().getColumns(specifier.catalog, specifier.schemaPattern, specifier.tableNamePattern, specifier.columnNamePattern);
            DBResultSetIO io = DBResultSetIO.of(results);
            debug("" + io);
            return io;
        }
    }

    /**
     * Logging stuff.
     */
    private void info(String mesage) { log.info(mesage);  }
    private void debug(String mesage) { log.debug(mesage);  }
}

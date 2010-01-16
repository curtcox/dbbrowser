package com.cve.io.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.concurrent.Immutable;

/**
 * Low-level access to result set meta data.
 * @author curt
 */
@Immutable
public final class DBResultSetMetaDataIO {

    public final int columnCount;
    public final ImmutableList<String> schemaNames;
    public final ImmutableList<String> tableNames;
    public final ImmutableList<String> columnNames;
    public final ImmutableList<String> catalogNames;

    /**
     * Use the factory
     */
    private DBResultSetMetaDataIO(
        final List<String> schemaNames, final List<String> tableNames,
        final List<String> columnNames, final List<String> catalogNames)
    {
        this.schemaNames  = copyOf(schemaNames);
        this.tableNames   = copyOf(tableNames);
        this.columnNames  = copyOf(columnNames);
        this.catalogNames = copyOf(catalogNames);
        columnCount = schemaNames.size();
        if (tableNames.size() != columnCount ||
            columnNames.size() != columnCount ||
            catalogNames.size() != columnCount
            )
        {
            throw new IllegalArgumentException();
        }
    }

    private static ImmutableList<String> copyOf(List<String> list) {
        return ImmutableList.copyOf(list);
    }

    /**
     * ImmutableLists can't conatin nulls (I don't know why, but it's in the 
     * docs)
     */
    private static String nullSafe(String string) {
        if (string==null) {
            return "";
        }
        return string;
    }

    static DBResultSetMetaDataIO of(ResultSetMetaData meta) {
        try {
            int count = meta.getColumnCount();
            List<String> schemas  = Lists.newArrayList();
            List<String> tables   = Lists.newArrayList();
            List<String> columns  = Lists.newArrayList();
            List<String> catalogs = Lists.newArrayList();
            for (int i=1; i<=count; i++) {
                schemas.add(nullSafe(meta.getSchemaName(i)));
                tables.add(nullSafe(meta.getTableName(i)));
                columns.add(nullSafe(meta.getColumnName(i)));
                catalogs.add(nullSafe(meta.getCatalogName(i)));
            }
            return new DBResultSetMetaDataIO(schemas,tables,columns,catalogs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "<DBResultSetMetaDataIO>" +
                  " schemaNames=" + schemaNames +
                  " tableNames=" + tableNames +
                  " columnNames=" + columnNames +
                  " catalogNames=" + catalogNames +
               "<DBResultSetMetaDataIO>";
    }
}

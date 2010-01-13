package com.cve.io.db;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.sql.ResultSet;
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
        List<String> schemaNames, List<String> tableNames, List<String> columnNames, List<String> catalogNames)
    {
        this.schemaNames  = ImmutableList.copyOf(schemaNames);
        this.tableNames   = ImmutableList.copyOf(tableNames);
        this.columnNames  = ImmutableList.copyOf(columnNames);
        this.catalogNames = ImmutableList.copyOf(catalogNames);
        columnCount = schemaNames.size();
        if (tableNames.size() != columnCount ||
            columnNames.size() != columnCount ||
            catalogNames.size() != columnCount
            )
        {
            throw new IllegalArgumentException();
        }
    }

    static DBResultSetMetaDataIO of(ResultSet results) {
        try {
            ResultSetMetaData meta = results.getMetaData();
            int count = meta.getColumnCount();
            List<String> schemas  = Lists.newArrayList();
            List<String> tables   = Lists.newArrayList();
            List<String> columns  = Lists.newArrayList();
            List<String> catalogs = Lists.newArrayList();
            for (int i=1; i<=count; i++) {
                schemas.add(meta.getSchemaName(i));
                tables.add(meta.getTableName(i));
                columns.add(meta.getColumnName(i));
                catalogs.add(meta.getCatalogName(i));
            }
            return new DBResultSetMetaDataIO(schemas,tables,columns,catalogs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                results.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

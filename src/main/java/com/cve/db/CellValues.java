package com.cve.db;

import com.google.common.collect.ImmutableMap;
import javax.annotation.concurrent.Immutable;

/**
 * Convenient static methods for creating a map of {@link Cell} values.
 */
@Immutable
public final class CellValues {

    public static ImmutableMap<Cell, DBValue> of(DBRow row, DBColumn column, DBValue value) {
        Cell cell = Cell.at(row,column);
        return ImmutableMap.of(cell,value);
    }

    public static ImmutableMap<Cell, DBValue> of(
        DBColumn c1, DBColumn c2, DBColumn c3, DBColumn c4,
        DBRow r1, DBRow r2,
             Object v1, Object v2, Object v3, Object v4,
             Object v5, Object v6, Object v7, Object v8
        )
    {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        builder.put(Cell.at(r1,c1), DBValue.of(v1));
        builder.put(Cell.at(r1,c2), DBValue.of(v2));
        builder.put(Cell.at(r1,c3), DBValue.of(v3));
        builder.put(Cell.at(r1,c4), DBValue.of(v4));
        builder.put(Cell.at(r2,c1), DBValue.of(v5));
        builder.put(Cell.at(r2,c2), DBValue.of(v6));
        builder.put(Cell.at(r2,c3), DBValue.of(v7));
        builder.put(Cell.at(r2,c4), DBValue.of(v8));
        return builder.build();
    }

    
}

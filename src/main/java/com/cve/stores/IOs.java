package com.cve.stores;

import com.cve.db.DBTable;

/**
 *
 * @author Curt
 */
public final class IOs {

    public static MapIO tableListToColumnList() {
        return MapIO.of(ListIO.of(TableIO.of()),ListIO.of(ColumnIO.of()));
    }

    public static MapIO tableListToJoinList() {
        return MapIO.of( ListIO.of(TableIO.of()) , ListIO.of(JoinIO.of()) );
    }

    public static MapIO serverToColumnList() {
        return MapIO.of(ServerIO.of(),ListIO.of(ColumnIO.of()));
    }

    public static MapIO serverToDatabaseList() {
        return MapIO.of(ServerIO.of(),ListIO.of(DatabaseIO.of()));
    }

    public static MapIO databaseToTableList() {
        return MapIO.of(DatabaseIO.of(),ListIO.of(TableIO.of()));
    }

    public static MapIO tableToLong() {
        return MapIO.of(TableIO.of(),LongIO.of());
    }

    public static MapIO stringToString() {
        return MapIO.of(StringIO.of(),StringIO.of());
    }
}

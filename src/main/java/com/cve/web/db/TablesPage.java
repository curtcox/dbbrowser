package com.cve.web.db;

import com.cve.model.db.DBColumn;
import com.cve.web.*;
import com.cve.model.db.Database;
import com.cve.model.db.DBServer;
import com.cve.model.db.DBTable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import static com.cve.util.Check.notNull;

/**
 * For picking a table.
 */
public final class TablesPage implements Model {

    /**
     * The server the tables come from.
     */
    final DBServer server;

    /**
     * The database the tables come from.
     */
    final Database database;

    final ImmutableList<DBTable> tables;

    final ImmutableMultimap<DBTable,DBColumn> columns;

    final ImmutableMap<DBTable,Long> rows;

    TablesPage(DBServer server, Database database,
        ImmutableList<DBTable> tables, ImmutableMap<DBTable,Long> rows,
        ImmutableMultimap<DBTable,DBColumn> columns)
    {
        this.server    = notNull(server);
        this.database  = notNull(database);
        this.tables    = notNull(tables);
        this.rows      = notNull(rows);
        this.columns   = notNull(columns);
        if (!rows.keySet().equals(columns.keySet())) {
            throw new IllegalArgumentException();
        }
    }
}

package com.cve.stores.db;

import com.cve.model.db.DBColumn;
import com.cve.model.db.Hints;
import com.cve.stores.Store;
import com.google.common.collect.ImmutableList;

/**
 * The {@link Hints} we know about.
 * There are several sources for hints.
 * <ol>
 * <li> Foreign key relationships defined on the database.
 * <li> Hints we have put in the store "manually" -- like with a hint file.
 * <li> Hints we have mined from:
 *    <ol>
 *        <li> the existing database data
 *        <li> SQL in log files or source code
 *    </ol>
 * </ol>
 */
public interface DBHintsStore extends Store<ImmutableList<DBColumn>,Hints> {}

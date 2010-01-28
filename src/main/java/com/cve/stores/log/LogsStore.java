package com.cve.stores.log;

import com.cve.log.LogEntry;
import com.cve.stores.Store;
import com.google.common.collect.ImmutableList;
/**
 * The logs we know about.
 */
public interface LogsStore extends Store<Class,ImmutableList<LogEntry>> {}
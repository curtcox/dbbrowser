package com.cve.stores.log;

import com.cve.log.LogEntry;
import com.cve.stores.Store;
import com.cve.web.PageRequest;
import com.google.common.collect.ImmutableList;

/**
 * The logs we know about.
 */
public interface PageRequestsStore extends Store<PageRequest.ID,ImmutableList<LogEntry>> {}


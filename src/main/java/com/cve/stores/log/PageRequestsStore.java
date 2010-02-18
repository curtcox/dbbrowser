package com.cve.stores.log;

import com.cve.log.LogEntry;
import com.cve.stores.Store;
import com.cve.web.core.PageRequestProcessor;
import com.google.common.collect.ImmutableList;

/**
 * The logs we know about.
 */
public interface PageRequestsStore extends Store<PageRequestProcessor,ImmutableList<LogEntry>> {}


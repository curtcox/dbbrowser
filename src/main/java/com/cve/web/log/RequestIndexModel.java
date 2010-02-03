package com.cve.web.log;

import com.cve.log.LogEntry;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.PageRequestProcessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
/**
 *
 * @author curt
 */
public final class RequestIndexModel implements Model {

    public final ImmutableList<PageRequestProcessor> requests;

    public final ImmutableMultimap<PageRequestProcessor, LogEntry>  entries;

    private RequestIndexModel(List<PageRequestProcessor> requests, Multimap<PageRequestProcessor, LogEntry>  entries) {
        this.requests = ImmutableList.copyOf(requests);
        this.entries  = ImmutableMultimap.copyOf(entries);
    }

    public static RequestIndexModel of(List<PageRequestProcessor> requests, Multimap<PageRequestProcessor, LogEntry>  entries) {
        return new RequestIndexModel(requests,entries);
    }

    @Override
    public String toString() {
        return "<RequestIndexModel>" +
                  " requests=" + requests +
               "</RequestIndexModel>";
    }
}

package com.cve.web.log;

import com.cve.log.LogEntry;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
/**
 *
 * @author curt
 */
public final class RequestIndexModel implements Model {

    public final ImmutableList<PageRequest.ID> requests;

    public final ImmutableMultimap<PageRequest.ID,LogEntry>  entries;

    private RequestIndexModel(List<PageRequest.ID> requests, Multimap<PageRequest.ID,LogEntry>  entries) {
        this.requests = ImmutableList.copyOf(requests);
        this.entries  = ImmutableMultimap.copyOf(entries);
    }

    public static RequestIndexModel of(List<PageRequest.ID> requests, Multimap<PageRequest.ID,LogEntry>  entries) {
        return new RequestIndexModel(requests,entries);
    }

    @Override
    public String toString() {
        return "<RequestIndexModel>" +
                  " requests=" + requests +
               "</RequestIndexModel>";
    }
}

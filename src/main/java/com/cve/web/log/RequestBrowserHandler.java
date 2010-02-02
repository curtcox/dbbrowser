package com.cve.web.log;

import com.cve.log.Log;
import com.cve.log.LogEntry;
import com.cve.log.Logs;
import com.cve.web.AbstractRequestHandler;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Handle requests for an object.
 * @author curt
 */
final class RequestBrowserHandler extends AbstractRequestHandler {

    private final Log log = Logs.of();
    
    private RequestBrowserHandler() {
        super("^/request/");
    }

    static RequestBrowserHandler of() {
        return new RequestBrowserHandler();
    }

    @Override
    public Model get(PageRequest request) {
        try {
            RequestModel requestModel = getRequest(request);
            if (!requestModel.entries.isEmpty()) {
                return requestModel;
            }
        } catch (NumberFormatException e) {
            log.warn(e);
        }
        return getIndex();
    }

    RequestIndexModel getIndex() {
        Set<PageRequest.ID> requests = Sets.newHashSet();
        Multimap entries = HashMultimap.create();
        for (Object object : ObjectRegistry.values()) {
            if (object instanceof LogEntry) {
                LogEntry entry = (LogEntry) object;
                PageRequest.ID id = entry.request;
                requests.add(id);
                entries.put(id, entry);
            }
        }
        List<PageRequest.ID> sorted = Lists.newArrayList(requests); 
        Collections.sort(sorted);
        return RequestIndexModel.of(sorted,entries);
    }

    RequestModel getRequest(PageRequest request) {
        String uri = request.requestURI;
        String idString = uri.substring(uri.lastIndexOf("/") + 1);
        PageRequest.ID id = PageRequest.ID.parse(idString);
        List<LogEntry> entries = Lists.newArrayList();
        for (Object object : ObjectRegistry.values()) {
            if (object instanceof LogEntry) {
                LogEntry entry = (LogEntry) object;
                if (entry.request.equals(id)) {
                    entries.add(entry);
                }
            }
        }
        Collections.sort(entries);
        return RequestModel.of(id, entries);
    }

}

package com.cve.web.management;

import com.cve.lang.RegEx;
import com.cve.log.Log;
import com.cve.log.LogEntry;
import com.cve.log.Logs;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageRequestProcessor;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestRouterServlet;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles requests for PageRequests.
 * In other words, this is used for viewing the server log of this server.
 * @author curt
 */
final class PageRequestBrowserHandler extends AbstractRequestHandler {

    private final Log log = Logs.of();
    
    private PageRequestBrowserHandler() {
        super(RegEx.of("^/request/"));
    }

    static PageRequestBrowserHandler of() {
        return new PageRequestBrowserHandler();
    }

    @Override
    public Model get(PageRequest request) {
        try {
            PageRequestServiceModel requestModel = getRequestModel(request);
            if (!requestModel.entries.isEmpty()) {
                return requestModel;
            }
        } catch (NumberFormatException e) {
            log.warn(e);
        }
        return getIndex();
    }

    PageRequestIndexModel getIndex() {
        Set<PageRequestProcessor> requests = Sets.newHashSet();
        Multimap<PageRequestProcessor,LogEntry> entries = HashMultimap.create();
        for (Object object : ObjectRegistry.values()) {
            if (object instanceof LogEntry) {
                LogEntry entry = (LogEntry) object;
                PageRequestProcessor id = entry.request;
                requests.add(id);
                entries.put(id, entry);
            }
        }
        List<PageRequestProcessor> sorted = Lists.newArrayList(requests);
        Collections.sort(sorted);
        return PageRequestIndexModel.of(sorted,getPagesFor(entries));
    }

    Map<PageRequestProcessor, PageRequestServiceModel> getPagesFor(Multimap<PageRequestProcessor,LogEntry> entries) {
        Map<PageRequestProcessor, PageRequestServiceModel> pages = Maps.newHashMap();
        for (PageRequestProcessor processor : entries.keySet()) {
            pages.put(processor,getPageFrom(processor,entries.get(processor)));
        }
        return pages;
    }

    PageRequestServiceModel getPageFrom(PageRequestProcessor processor, Collection<LogEntry> entries) {
        PageRequest   request = PageRequest.NULL;
        PageResponse response = PageResponse.NULL;
        for (LogEntry entry : entries) {
            if (entry.logger.equals(RequestRouterServlet.class)) {
                for (Object arg : entry.args) {
                    if (arg instanceof PageRequest) {
                        request = (PageRequest) arg;
                    }
                    if (arg instanceof PageResponse) {
                        response = (PageResponse) arg;
                    }
                }
            }
        }
        return PageRequestServiceModel.of(processor, request, response, entries);
    }

    /**
     * Use the ObjectRegistry to generate to model for the given request.
     */
    PageRequestServiceModel getRequestModel(PageRequest request) {
        String uri = request.requestURI;
        String idString = uri.substring(uri.lastIndexOf("/") + 1);
        PageRequestProcessor id = PageRequestProcessor.parse(idString);
        PageResponse response = PageResponse.NULL;
        List<LogEntry> entries = Lists.newArrayList();
        for (Object object : ObjectRegistry.values()) {
            if (object instanceof LogEntry) {
                LogEntry entry = (LogEntry) object;
                if (entry.request.equals(id)) {
                    entries.add(entry);
                    for (Object arg : entry.args) {
                        if (arg instanceof PageResponse) {
                            response = (PageResponse) arg;
                        }
                    }
                }
            }
        }
        Collections.sort(entries);
        return PageRequestServiceModel.of(id, request, response, entries);
    }



}

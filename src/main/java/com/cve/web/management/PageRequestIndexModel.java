package com.cve.web.management;

import com.cve.web.Model;
import com.cve.web.PageRequestProcessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
/**
 * Index of PageRequests against this web server.
 * @author curt
 */
public final class PageRequestIndexModel implements Model {

    /**
     * The HTTP requests.
     */
    public final ImmutableList<PageRequestProcessor> requests;

    /**
     * What happened with them.
     */
    public final ImmutableMap<PageRequestProcessor, PageRequestServiceModel>  pages;

    private PageRequestIndexModel(List<PageRequestProcessor> requests, Map<PageRequestProcessor, PageRequestServiceModel>  entries) {
        this.requests = ImmutableList.copyOf(requests);
        this.pages  = ImmutableMap.copyOf(entries);
    }

    public static PageRequestIndexModel of(List<PageRequestProcessor> requests, Map<PageRequestProcessor, PageRequestServiceModel>  entries) {
        return new PageRequestIndexModel(requests,entries);
    }

    @Override
    public String toString() {
        return "<RequestIndexModel>" +
                  " requests=" + requests +
               "</RequestIndexModel>";
    }
}

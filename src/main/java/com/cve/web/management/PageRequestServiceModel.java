package com.cve.web.management;

import com.cve.log.LogEntry;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageRequestProcessor;
import com.cve.web.core.PageResponse;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import static com.cve.util.Check.notNull;
/**
 * How a page request was serviced.
 * @author curt
 */
public final class PageRequestServiceModel implements Model {

    /**
     * The request that was serviced.
     */
    public final PageRequestProcessor id;

    /**
     * The request that was made
     */
    public final PageRequest request;

    /**
     * How we responded.
     */
    public final PageResponse response;

    /**
     * What happened along the way.
     */
    public final ImmutableList<LogEntry> entries;

    private PageRequestServiceModel(
        PageRequestProcessor id, PageRequest request, PageResponse response, Collection<LogEntry> entries)
    {
        this.id       = notNull(id);
        this.request  = notNull(request);
        this.response = notNull(response);
        this.entries  = ImmutableList.copyOf(entries);
    }

    public static PageRequestServiceModel of(PageRequestProcessor id, PageRequest request, PageResponse response, Collection<LogEntry> entries) {
        return new PageRequestServiceModel(id,request,response,entries);
    }

}

package com.cve.web.log;

import com.cve.log.LogEntry;
import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.cve.web.PageRequestProcessor;
import com.google.common.collect.ImmutableList;
import java.util.List;
import static com.cve.util.Check.notNull;
/**
 *
 * @author curt
 */
public final class RequestModel implements Model {

    public final PageRequestProcessor id;

    public final ImmutableList<LogEntry> entries;

    private RequestModel(PageRequestProcessor id, List<LogEntry> entries) {
        this.id = notNull(id);
        this.entries = ImmutableList.copyOf(entries);
    }

    public static RequestModel of(PageRequestProcessor id, List<LogEntry> entries) {
        return new RequestModel(id,entries);
    }

}
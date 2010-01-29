package com.cve.web.log;

import com.cve.web.Model;
import com.cve.web.PageRequest;
import com.google.common.collect.ImmutableList;
import java.util.List;
/**
 *
 * @author curt
 */
public final class RequestIndexModel implements Model {

    public final ImmutableList<PageRequest.ID> requests;

    private RequestIndexModel(List<PageRequest.ID> requests) {
        this.requests = ImmutableList.copyOf(requests);
    }

    public static RequestIndexModel of(List<PageRequest.ID> requests) {
        return new RequestIndexModel(requests);
    }

    @Override
    public String toString() {
        return "<RequestIndexModel>" +
                  " requests=" + requests +
               "</RequestIndexModel>";
    }
}

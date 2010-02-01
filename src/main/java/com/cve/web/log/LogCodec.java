package com.cve.web.log;

import com.cve.log.LogEntry;
import com.cve.web.PageRequest.ID;
import java.net.URI;

/**
 *
 * @author curt
 */
public final class LogCodec {

    final ObjectLink links = ObjectLink.of();

    private LogCodec() {}

    public static LogCodec of() {
        return new LogCodec();
    }

    public URI encode(ID id) {
        return links.uri(id);
    }

    public URI encode(LogEntry entry) {
        return links.uri(entry);
    }

}

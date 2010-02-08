package com.cve.web.log;

import com.cve.log.LogEntry;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.util.URIs;
import com.cve.web.PageRequestProcessor;
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

    public URI encode(PageRequestProcessor id) {
        return URIs.of("/request/" + Long.toHexString(id.timestamp.value));
    }

    public URI encode(LogEntry entry) {
        return links.uri(entry);
    }

    public URI encode(AnnotatedStackTrace trace) {
        return links.uri(trace);
    }

}

package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Timestamp;
import com.cve.web.PageRequest;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 *
 * @author curt
 */
@Immutable
public final class LogEntry {

    public final Timestamp timeStamp = Timestamp.of();

    public final PageRequest request;

    public final AnnotatedStackTrace trace;

    public final String message;

    private LogEntry(PageRequest request, AnnotatedStackTrace trace, String message) {
        this.request = notNull(request);
        this.trace   = notNull(trace);
        this.message = notNull(message);
    }
}

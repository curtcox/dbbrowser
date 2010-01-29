package com.cve.log;

import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Timestamp;
import com.cve.web.PageRequest;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A single entry for a log.
 * @author curt
 */
@Immutable
public final class LogEntry implements Comparable<LogEntry> {

    public final LogLevel level;

    public final Timestamp timeStamp = Timestamp.of();

    public final PageRequest.ID request;

    public final AnnotatedStackTrace trace;

    public final String message;

    private LogEntry(LogLevel level, PageRequest.ID request, AnnotatedStackTrace trace, String message) {
        this.level   = notNull(level);
        this.request = notNull(request);
        this.trace   = notNull(trace);
        this.message = notNull(message);
    }

    public static LogEntry of(LogLevel level, PageRequest.ID request, AnnotatedStackTrace trace, String message) {
        return new LogEntry(level,request,trace,message);
    }

    @Override
    public int compareTo(LogEntry t) {
        return timeStamp.compareTo(t.timeStamp);
    }
}

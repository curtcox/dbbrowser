package com.cve.log;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Timestamp;
import com.cve.web.PageRequest;
import com.cve.web.log.LogCodec;
import java.net.URI;
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

    final LogCodec codec = LogCodec.of();

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

    public Link linkTo() {
        Label text = Label.of("" + level);
        URI target = codec.encode(this);
        return Link.textTarget(text, target);
    }
}

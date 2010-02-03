package com.cve.log;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.AnnotatedStackTrace;
import com.cve.util.Timestamp;
import com.cve.web.PageRequest;
import com.cve.web.PageRequestProcessor;
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

    public final PageRequestProcessor request;

    public final Class logger;

    public final AnnotatedStackTrace trace;

    public final String message;

    public final Object[] args;

    final LogCodec codec = LogCodec.of();

    static final Object[] NO_ARGS = new Object[0];

    private LogEntry(LogLevel level, PageRequestProcessor request, AnnotatedStackTrace trace, Class logger, String message, Object[] args) {
        this.level   = notNull(level);
        this.request = notNull(request);
        this.trace   = notNull(trace);
        this.logger  = notNull(logger);
        this.message = notNull(message);
        this.args    = notNull(args);
    }

    public static LogEntry of(LogLevel level, PageRequestProcessor request, AnnotatedStackTrace trace, Class logger, String message) {
        return new LogEntry(level,request,trace,logger,message,NO_ARGS);
    }

    public static LogEntry of(LogLevel level, PageRequestProcessor request, AnnotatedStackTrace trace, Class logger, String message, Object[] args) {
        return new LogEntry(level,request,trace,logger,message,args);
    }

    @Override
    public int compareTo(LogEntry t) {
        return timeStamp.compareTo(t.timeStamp);
    }

    public Link linkTo() {
        Label text = Label.of(logger.getSimpleName() + ":" + level);
        URI target = codec.encode(this);
        return Link.textTarget(text, target);
    }
}

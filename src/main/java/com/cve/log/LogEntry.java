package com.cve.log;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.lang.AnnotatedStackTrace;
import com.cve.util.Timestamp;
import com.cve.web.core.PageRequestProcessor;
import com.cve.web.management.LogCodec;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * A single entry for a log.
 * @author curt
 */
@Immutable
public final class LogEntry implements Comparable<LogEntry> {

    /**
     * How important this entry is.
     */
    public final LogLevel level;

    /**
     * When this entry was made.
     */
    public final Timestamp timeStamp = Timestamp.of();

    /**
     * The request being served when the entry was made.
     */
    public final PageRequestProcessor request;

    /**
     * The class that requested the entry.
     */
    public final Class logger;

    /**
     * Where this was logged from.
     */
    public final AnnotatedStackTrace trace;

    /**
     * The message to log.
     */
    public final String message;

    /**
     * Arguments associated with this entry.
     */
    public final Object[] args;

    /**
     * For mapping to and from URLs dealing with logging.
     */
    final LogCodec codec = LogCodec.of();

    /**
     * Used for args when log entries have no args.
     */
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

package com.cve.web.core;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.util.Check;
import com.cve.util.Timestamp;
import com.cve.web.management.LogCodec;
import java.net.URI;
import javax.annotation.concurrent.Immutable;

/**
 * The basic idea is that one of these corresponds to each HTTP request.
 * <p>
 * In other words, the thing that processes a page request.
 * This is sort of like a thread id.  It is different, however, because web
 * servers will tend to reuse the same thread to process multiple sequential requests.
 * A single PageRequestProcessor can have many PageRequests, because one
 * PageRequest can wrap another.
 * 
 */
@Immutable
public final class PageRequestProcessor implements Comparable<PageRequestProcessor> {

    /**
     * When this processor was created.
     */
    public final Timestamp timestamp;

    /**
     * Where we log stuff.
     */
    static final Log log = Logs.of();

    /**
     * For creating links to this object.
     */
    final LogCodec codec = LogCodec.of();

    /**
     * Processors for all of the current threads.
     */
    private static final ThreadLocal<PageRequestProcessor> local = new ThreadLocal() {
        @Override
        protected PageRequestProcessor initialValue() {
            return new PageRequestProcessor();
        }
    };

    private PageRequestProcessor() {
        timestamp = Timestamp.of();
    }

    private PageRequestProcessor(Timestamp timestamp) {
        this.timestamp = Check.notNull(timestamp);
    }

    /**
     * Start using a new request.
     * The thread local isn't enough.  Web servers will tend to use thread
     * pools, so thread doesn't really imply request.
     */
    static void next() {
        local.set(new PageRequestProcessor());
    }

    public static PageRequestProcessor of() {
        return local.get();
    }

    public static PageRequestProcessor parse(String string) {
        log.args(string);
        long code = Long.parseLong(string, 16);
        return new PageRequestProcessor(Timestamp.of(code));
    }

    @Override
    public int compareTo(PageRequestProcessor other) {
        return timestamp.compareTo(other.timestamp);
    }

    @Override
    public String toString() {
        return "id=" + timestamp;
    }

    public Link linkTo() {
        Label text = Label.of("" + timestamp.value);
        URI target = codec.encode(this);
        return Link.textTarget(text, target);
    }

    @Override
    public int hashCode() {
        return timestamp.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        PageRequestProcessor other = (PageRequestProcessor) o;
        return timestamp.equals(other.timestamp);
    }
}

package com.cve.web.management;

import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;

/**
 * For providing hyperlinks to expose objects in the JVM.
 * @author Curt
 */
@Immutable
public final class ObjectLink {

    public static ObjectLink NULL;

    public final String text;

    public final Object target;

    public final URI uri;

    private ObjectLink(Object target, String text, URI uri) {
        this.target = notNull(target);
        this.text = notNull(text);
        this.uri = notNull(uri);
    }

    static ObjectLink of(Object target, String text, URI uri) {
        return new ObjectLink(target,text,uri);
    }
}

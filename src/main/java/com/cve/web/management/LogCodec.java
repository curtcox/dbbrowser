package com.cve.web.management;

import com.cve.lang.AnnotatedClass;
import com.cve.lang.Strings;
import com.cve.util.URIs;
import com.cve.web.core.PageRequestProcessor;
import java.net.URI;

/**
 * For converting between objects and management URIs.
 * @author curt
 */
public final class LogCodec {

    /**
     * The URL we use for null.
     */
    static final String NULL_OBJECT = "/object/null";

    private LogCodec() {}

    public static LogCodec of() {
        return new LogCodec();
    }

    public URI encode(PageRequestProcessor id) {
        return URIs.of("/request/" + Long.toHexString(id.timestamp.value));
    }

    /**
     * Return a labeled link to the given object.
     */
    public URI encode(Object object) {
        if (object==null) {
            return URIs.of(NULL_OBJECT);
        }
        if (object instanceof Class) {
            Class c = (Class) object;
            return URIs.of("/object/" + urlFragment(c));
        }
        String hash = ObjectRegistry.put(object).toHexString();
        return URIs.of("/object/" + urlFragment(object.getClass()) + "/"+ hash);
    }

    public Object decode(URI uri) {
        String uriString = uri.toString();
        if (uriString.equals(NULL_OBJECT)) {
            return null;
        }
        String tail = uriString.substring("/object/".length());
        // is it an object?
        try {
            String hex  = Strings.afterLast(tail,"/");
            UniqueObjectKey key = UniqueObjectKey.parse(hex);
            return ObjectRegistry.get(key);
        } catch (NumberFormatException e) {}
        // Is it a class?
        try {
            String className = tail.replace("/",".");
            Class c = Class.forName(className);
            return AnnotatedClass.of(c);
        } catch (ClassNotFoundException e) {}
        String message = "" + uri;
        throw new IllegalArgumentException(message);
    }

    /**
     * Return the given class as a URL fragment
     */
    static String urlFragment(Class c) {
        return c.getName().replace(".", "/")
                          .replace("[B", "")
                          .replace("[L", "");
    }


}

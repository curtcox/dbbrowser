package com.cve.web.management;

import com.cve.lang.URIObject;
import com.cve.log.Log;
import com.cve.log.Logs;
import com.cve.ui.UILink;


/**
 * For providing hyperlinks to expose objects in the JVM.
 * @author Curt
 */
public final class ObjectLinks {

    final LogCodec codec = LogCodec.of();
    
    final Log log = Logs.of();

    private ObjectLinks() {}

    public static ObjectLinks of() {
        return new ObjectLinks();
    }

    /**
     * Return the label for the URL, or an empty string if there is none.
     */
    static String getLabel(Object object) {
        if (object==null) {
            return "null";
        }
        if (object instanceof Class) {
            return object.toString();
        }
        return object.getClass().toString();
    }


    /**
     * Return a labeled link to the given object.
     */
    public UILink to(Object object) {
        String hash = ObjectRegistry.put(object).toHexString();
        String text = getLabel(object) + "/" + hash;
        URIObject target = codec.encode(object);
        return UILink.textTarget(text, target);
    }


    /**
     * Return a labeled link to the given object.
     */
    public UILink to(String text, Object object) {
        URIObject target = codec.encode(object);
        return UILink.textTarget(text, target);
    }

    /**
     * Quote the given string
     */
    private static String q(String text) {
        return "\"" + text + "\"";
    }

}

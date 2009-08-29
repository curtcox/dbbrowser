package com.cve.web.log;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.URIs;
import java.net.URI;

/**
 * For providing hyperlinks to expose objects in the JVM.
 * @author Curt
 */
public final class ObjectLink {

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
    public static String to(Object object) {
        String hash = ObjectRegistry.put(object).toHexString();
        Label text = Label.of(getLabel(object) + "/" + hash);
        URI target = URIs.of("/object/" + hash);
        return Link.textTarget(text, target).toString();
    }

    /**
     * Return a labeled link to the given object.
     */
    public static String to(String labelText, Object object) {
        String hash = ObjectRegistry.put(object).toHexString();
        Label text = Label.of(labelText);
        URI target = URIs.of("/object/" + hash);
        return Link.textTarget(text, target).toString();
    }

}

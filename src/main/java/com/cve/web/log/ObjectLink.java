package com.cve.web.log;

import com.cve.html.Label;
import com.cve.log.Log;
import com.cve.util.URIs;
import java.net.URI;
import static com.cve.util.Check.notNull;

/**
 * For providing hyperlinks to expose objects in the JVM.
 * @author Curt
 */
public final class ObjectLink {

    final Log log;

    private ObjectLink(Log log) {
        this.log = notNull(log);
    }

    public static ObjectLink of(Log log) {
        return new ObjectLink(log);
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
    public String to(Object object) {
        String hash = ObjectRegistry.put(object).toHexString();
        Label text = Label.of(getLabel(object) + "/" + hash,log);
        URI target = URIs.of("/object/" + hash);
        return link(text, target).toString();
    }

    /**
     * Return a labeled link to the given object.
     */
    public String to(String labelText, Object object) {
        String hash = ObjectRegistry.put(object).toHexString();
        Label text = Label.of(labelText,log);
        URI target = URIs.of("/object/" + hash);
        return link(text, target).toString();
    }

    private static String link(Label text, URI target) {
        return "<a href=" + q(target.toString()) + ">" + text +"</a>";
    }

    /**
     * Quote the given string
     */
    private static String q(String text) {
        return "\"" + text + "\"";
    }

}

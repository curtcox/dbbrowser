package com.cve.web.management;

import com.cve.html.Label;
import com.cve.log.Log;
import com.cve.log.Logs;
import java.net.URI;

/**
 * For providing hyperlinks to expose objects in the JVM.
 * @author Curt
 */
public final class ObjectLink {

    final LogCodec codec = LogCodec.of();
    
    final Log log = Logs.of();

    private ObjectLink() {}

    public static ObjectLink of() {
        return new ObjectLink();
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
        Label text = Label.of(getLabel(object) + "/" + hash);
        URI target = codec.encode(object);
        return link(text, target).toString();
    }


    /**
     * Return a labeled link to the given object.
     */
    public String to(String labelText, Object object) {
        Label text = Label.of(labelText);
        URI target = codec.encode(object);
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

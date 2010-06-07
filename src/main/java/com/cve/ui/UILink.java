package com.cve.ui;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.lang.URIObject;
import static com.cve.util.Check.notNull;
import com.cve.web.management.LogCodec;
import com.cve.web.management.ObjectRegistry;


/**
 * Like a HTML hyperlink.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UILink implements UIElement {

    public final URIObject target;

    public final String text;

    public final URIObject image;

    public final String alt;

    public static UILink NULL;

    /**
     * Use a factory.
     * @param value
     */
    private UILink(URIObject target, String text, URIObject image, String alt) {
        this.target = notNull(target);
        this.text   = notNull(text);
        this.image  = image;
        this.alt    = alt;
    }

    /**
     * Return a link to the given object.
     */
    public static UILink to(Object object) {
        String hash = ObjectRegistry.put(object).toHexString();
        String text = getLabel(object) + "/" + hash;
        return to(text,object);
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
     * Return a link to the given object.
     */
    public static UILink to(String text, Object object) {
        LogCodec codec = LogCodec.of();
        URIObject target = codec.encode(object);
        return new UILink(target,text,null,null);
    }

    public static UILink textTarget(String text, URIObject target) {
        return new UILink(target,text,null,null);
    }

    public static UILink textTargetImageAlt(String text, URIObject target, URIObject image, String alt) {
        return new UILink(target,text,image,alt);
    }

    @Override
    public String toString() {
        Label label = Label.of(text);
        if (image!=null) {
            return Link.textTargetImageAlt(label, target, image, alt).toString();
        }
        return Link.textTarget(label, target).toString();
    }
}

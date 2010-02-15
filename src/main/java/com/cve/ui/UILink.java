package com.cve.ui;

import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.util.Check;
import com.cve.web.management.ObjectLink;
import java.net.URI;

/**
 * Like a HTML hyperlink.
 * By constructing an object graph, rather than a string, it will be easier
 * to render to something other than HTML later.
 */
public final class UILink implements UIElement {

    private final String value;

    /**
     * Use a factory.
     * @param value
     */
    private UILink(String value) {
        this.value = Check.notNull(value);
    }

    /**
     * Return a link to the given object.
     */
    public static UILink to(Object o) {
        return new UILink(ObjectLink.of().to(o));
    }

    /**
     * Return a link to the given object.
     */
    public static UILink to(String label, Object o) {
        return new UILink(ObjectLink.of().to(label,o));
    }

    public static UILink textTarget(Label of, URI target) {
        String value = Link.textTarget(of, target).toString();
        return new UILink(value);
    }

    public static UILink textTargetImageAlt(Label text, URI target, URI image, String alt) {
        String value = Link.textTargetImageAlt(text, target, image, alt).toString();
        return new UILink(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

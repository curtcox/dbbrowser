package com.cve.html;

import com.cve.util.Replace;
import com.cve.web.DebugHandler;
import java.net.URI;
import javax.annotation.concurrent.Immutable;
import static com.cve.util.Check.notNull;
/**
 * For creating HTML links to a url with optional tooltips.
 * An external Javascript library is used for the tooltips.
 * See http://www.walterzorn.com/tooltip/tooltip_e.htm
 */
@Immutable

public final class Link {

    /**
     * Where this link goes.
     */
    private final URI  target;

    /**
     * Text that appears in the tool tip.
     */
    private final Tooltip tip;

    /**
     * HTML that will show the link.
     */
    private final String html;

    private Link(Label text, URI target, Tooltip tip) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = notNull(tip);
        this.html   = a(text,target,tip);
    }

    private Link(Label text, URI target, Tooltip tip, URI image) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = notNull(tip);
        this.html   = a(text,target,tip,image);
    }

    private Link(Label text, URI target) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = null;
        this.html   = a(text,target);
    }

    private Link(Label text, URI target, URI image, String alt) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = null;
        this.html   = a(text,target,image,alt);
    }

    private static String a(Label text, URI target, URI image, String alt) {
        return "<a href=" + q(target.toString()) + ">" + HTMLTags.of().img(alt,image) +"</a>" + debug();
    }

    private static String a(Label text, URI target) {
        return "<a href=" + q(target.toString()) + ">" + text +"</a>" + debug();
    }

    private static String a(Label text, URI target, Tooltip tip) {
        return "<a href=" + q(target.toString()) + " " +  tip(tip) + ">" + text +"</a>" + debug();
    }

    private static String a(Label text, URI target, Tooltip tip, URI image) {
        return "<a href=" + q(target.toString()) + " " + tip(tip) + ">" + HTMLTags.of().img(text.toString(),image) +"</a>" + debug();
    }

    /**
     * Return a debugging link, if debugging is on.
     * @return
     */
    private static String debug() {
        return DebugHandler.debugLink();
    }

    public static Link textTarget(Label text, URI target) {
        return new Link(text,target);
    }

    public static Link textTargetImageAlt(Label text, URI target, URI image, String alt) {
        return new Link(text,target,image,alt);
    }

    public static Link textTargetTipImage(Label text, URI target, Tooltip tip, URI image) {
        return new Link(text,target,tip,image);
    }

    public static Link textTargetTip(Label text, URI target, Tooltip tip) {
        if (tip==null) {
            return new Link(text,target);
        }
        return new Link(text,target,tip);
    }

    public URI getTarget() { return target; }

    /**
     * Create the Javascript that shows the tooltip
     */
    private static String tip(Tooltip tip) {
        String text = Replace.escapeQuotes(tip.toHTML().toString());
        return "onmouseover=" + q("Tip('" + text +  "', STICKY, 1)") + " onmouseout=" + q("UnTip()");
    }

    /**
     * Quote the given string
     */
    private static String q(String text) {
        return "\"" + text + "\"";
    }


    @Override
    public String toString() { return html; }

    /**
     * Return a new link with the given text.
     */
    public Link with(Label text) {
        return textTargetTip(text,target,tip);
    }
}

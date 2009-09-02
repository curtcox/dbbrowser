package com.cve.html;

import com.cve.util.Replace;
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
        this.html = "<a href=" + q(target.toString()) + " " +  tip(tip) + ">" + text +"</a>";
    }

    private Link(Label text, URI target, Tooltip tip, URI image) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = notNull(tip);
        this.html = "<a href=" + q(target.toString()) + " " + tip(tip) + ">" + img(text.toString(),image) +"</a>";
    }

    private Link(Label text, URI target) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = null;
        this.html = "<a href=" + q(target.toString()) + ">" + text +"</a>";
    }

    private Link(Label text, URI target, URI image, String alt) {
        notNull(text);
        this.target = notNull(target);
        this.tip    = null;
        this.html = "<a href=" + q(target.toString()) + ">" + img(alt,image) +"</a>";
    }

    public static Link textTarget(Label text, URI target) {
        return new Link(text,target);
    }

    public static Link textTargetImageAlt(Label text, URI target, URI image, String alt) {
        return new Link(text,target,image,alt);
    }

    static Link textTargetTipImage(Label text, URI target, Tooltip tip, URI image) {
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

    /**
     * Image for the given URI.
     * "Alt text is an alternative, not a tooltip"
     * See http://www.456bereastreet.com/archive/200604/alt_text_is_an_alternative_not_a_tooltip/
     */
    private static String img(String title, URI uri) {
        return Replace.bracketQuote("<img alt=[" + title + "] title=[" + title + "] src=[" + uri + "]>");
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

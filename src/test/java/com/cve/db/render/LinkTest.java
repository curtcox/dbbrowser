package com.cve.db.render;

import com.cve.html.Tooltip;
import com.cve.html.Label;
import com.cve.html.Link;
import com.cve.html.HTML;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

import com.cve.util.Replace;

/**
 *
 * @author curt
 */
public class LinkTest {

    public LinkTest() {}

    private static final Tooltip DUMMY_TOOLTIP = new Tooltip() {
        public HTML toHTML() { return HTML.of("dummy tooltip"); }
    };

    private String linkHTML() {
        Label text = Label.of("foo");
        URI target = uri("bar");
        Tooltip tip = DUMMY_TOOLTIP;
        String html = Link.textTargetTip(text,target,tip).toString();
        return html;
    }

    @Test
    public void fullLinkHTML() {
        String html = linkHTML();
        String expected = Replace.bracketQuote("<a href=[bar] onmouseover=[Tip('dummy tooltip', STICKY, 1)] onmouseout=[UnTip()]>foo</a>");
        assertEquals(expected,html);
    }

    @Test
    public void linkContainsLabel() {
        String html = linkHTML();
        assertTrue(html,html.contains("foo"));
    }

    @Test
    public void linkContainsTarget() {
        String html = linkHTML();
        assertTrue(html,html.contains("bar"));
    }

    @Test
    public void linkContainsTipText() {
        String html = linkHTML();
        assertTrue(html,html.contains(DUMMY_TOOLTIP.toHTML().toString()));
    }

    @Test
    public void linkContainsTip() {
        String html = linkHTML();
        assertTrue(html,html.contains("Tip("));
    }

    @Test
    public void linkContainsUnTip() {
        String html = linkHTML();
        assertTrue(html,html.contains("UnTip("));
    }

    @Test
    public void linkContainsOnMouseover() {
        String html = linkHTML();
        assertTrue(html,html.contains("onmouseover="));
    }

    @Test
    public void linkContainsOnMouseout() {
        String html = linkHTML();
        assertTrue(html,html.contains("onmouseout="));
    }

    private static URI uri(String text) {
        try {
            return new URI(text);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

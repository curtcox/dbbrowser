package com.cve.web;

import com.cve.log.Log;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class PageDecoratorTest {

    ;
    final PageDecorator decorator = PageDecorator.of(StringModelRenderer.of());
    final ClientInfo client = ClientInfo.of();

    @Test
    public void renderedContainsHtml() {
        HtmlPage body = HtmlPage.guts("");
        String rendered = decorator.render(body, client).toString();
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

}

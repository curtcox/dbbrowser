package com.cve.web;

import com.cve.log.Log;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class PageDecoratorTest {

    Log log;
    final PageDecorator decorator = PageDecorator.of(StringModelRenderer.of(log));
    final ClientInfo client = ClientInfo.of();

    @Test
    public void renderedContainsHtml() {
        HtmlPage body = HtmlPage.guts("",log);
        String rendered = decorator.render(body, client).toString();
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

}

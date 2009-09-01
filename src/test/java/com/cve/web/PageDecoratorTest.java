package com.cve.web;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class PageDecoratorTest {

    final PageDecorator decorator = PageDecorator.of(new StringModelRenderer());
    final ClientInfo client = ClientInfo.of();

    @Test
    public void renderedContainsHtml() {
        HtmlPage body = HtmlPage.body("");
        String rendered = decorator.render(body, client).toString();
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

    @Test
    public void whenSuppliedHeader() {
        HtmlPage body = HtmlPage.headBody("<head></head>","");
        String rendered = decorator.render(body, client).toString();
        assertTrue(rendered.contains("<head>"));
        assertTrue(rendered.contains("</head>"));
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

}

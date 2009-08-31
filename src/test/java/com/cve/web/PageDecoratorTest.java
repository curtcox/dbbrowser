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
        String body = "";
        String rendered = decorator.render(body, client);
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

    @Test
    public void whenSuppliedHeader() {
        String body = "<head></head>";
        String rendered = decorator.render(body, client);
        assertTrue(rendered.contains("<head>"));
        assertTrue(rendered.contains("</head>"));
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

}

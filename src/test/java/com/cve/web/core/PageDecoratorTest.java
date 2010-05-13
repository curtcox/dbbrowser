package com.cve.web.core;

import com.cve.ui.UIElement;
import com.cve.ui.UILabel;
import com.cve.web.core.renderers.StringModelRenderer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class PageDecoratorTest {

    
    final PageDecorator decorator = PageDecorator.of(StringModelRenderer.of());
    final ClientInfo client = ClientInfo.of();

    @Test
    public void renderedContainsHtml() {
        UIElement body = UILabel.of("");
        String rendered = decorator.render(body, client).toString();
        assertTrue(rendered.contains("<html>"));
        assertTrue(rendered.contains("</html>"));
    }

}

package com.cve.web.core;

import com.cve.ui.UIElement;
import com.cve.util.Check;
import com.cve.util.Replace;

/**
 * Adds the Javascript and CSS we need to the HTML pages we produce.
 */
public final class PageDecorator implements ModelHtmlRenderer {

    /**
     * For rendering the model into a page body.
     */
    private final ModelHtmlRenderer renderer;

    /**
     * Enable tooltips by including Javascript tooltip library.
     * http://www.walterzorn.com/tooltip/tooltip_e.htm
     */
    private static final String SCRIPTS = Replace.bracketQuote(
        "<script type=[text/javascript] src=[/resource/wz_tooltip/wz_tooltip.js]></script>");

    private PageDecorator(ModelHtmlRenderer renderer) {
        this.renderer = Check.notNull(renderer);
    }

    public static PageDecorator of(ModelHtmlRenderer renderer) {
        return new PageDecorator(renderer);
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        UIElement body = renderer.render(model,client);
        return render(body,client);
    }

    /**
     * Return the page with CSS and Javascript added.
     */
    UIElement render(UIElement page, ClientInfo client) {
        throw new UnsupportedOperationException();
        // return page.withGuts(SCRIPTS + CSS.SHEET + page.guts);
    }
}

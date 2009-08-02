package com.cve.web;

import com.cve.html.CSS;
import com.cve.util.Check;
import com.cve.util.Replace;

import static com.cve.html.HTML.*;
/**
 * Adds the Javascript and CSS we need to the HTML pages we produce.
 */
public final class PageDecorator implements ModelRenderer {

    private final ModelRenderer renderer;

    /**
     * Enable tooltips by including Javascript tooltip library.
     * http://www.walterzorn.com/tooltip/tooltip_e.htm
     */
    private static final String SCRIPTS = Replace.bracketQuote(
        "<script type=[text/javascript] src=[/resource/wz_tooltip/wz_tooltip.js]></script>");

    private PageDecorator(ModelRenderer renderer) {
        this.renderer = Check.notNull(renderer);
    }

    public static PageDecorator of(ModelRenderer renderer) {
        return new PageDecorator(renderer);
    }

    public Object render(Model model, ClientInfo client) {
        String body = (String) renderer.render(model,client);
        return render(body,client);
    }

    String render(String body, ClientInfo client) {
        return html(body(SCRIPTS + CSS.SHEET + body));
    }
}

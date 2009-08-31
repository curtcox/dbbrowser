package com.cve.web;

import com.cve.html.CSS;
import com.cve.util.Check;
import com.cve.util.Replace;

import static com.cve.html.HTML.*;
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
    public String render(Model model, ClientInfo client) {
        String body = (String) renderer.render(model,client);
        return render(body,client);
    }

    /**
     * Return the body with CSS and Javascript added.
     * In what feels like a very clumsy hack, a HTML head can be added to the
     * page by prepending it (including surrounding tags unlike the bare body we
     * take) to the body.
     * <p>
     * This is done to accomodate renderers like the one for free form queries,
     * which need to be able to specify a base page in the head.
     * It seemed like a better solution than complicating the interface for
     * this one edge case.
     */
    String render(String body, ClientInfo client) {
        if (!body.startsWith("<head>")) {
            return html(body(SCRIPTS + CSS.SHEET + body));
        }
        int end = body.indexOf("</head>") + "</head>".length();
        String head = body.substring(0,end);
        String newBody = body.substring(end);
        return html(head + body(SCRIPTS + CSS.SHEET + newBody));
    }
}

package com.cve.web.core.pages;

import com.cve.ui.HTMLTags;
import com.cve.ui.UIElement;
import com.cve.web.core.ClientInfo;
import com.cve.web.core.HtmlPage;
import com.cve.web.core.Model;
import com.cve.web.core.ModelHtmlRenderer;
import com.cve.web.core.Page;
import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.web.core.RequestHandler;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.management.browsers.ObjectBrowser;
import static com.cve.util.Check.notNull;

/**
 * A skeletal page implementation.
 * @author curt
 */
public abstract class AbstractPage implements Page {

    /**
     * Handles the page request, unless produce is overridden.
     */
    final RequestHandler handler;

    /**
     * Renders the model, unless render is overridden.
     */
    final ModelHtmlRenderer renderer;

    /**
     * Use this if you don't intend to override any methods.
     */
    protected AbstractPage(RequestHandler handler, ModelHtmlRenderer renderer) {
        this.handler  = notNull(handler);
        this.renderer = notNull(renderer);
    }

    /**
     * Use this if you intend to override render.
     */
    protected AbstractPage(RequestHandler handler) {
        this.handler  = notNull(handler);
        this.renderer = null;
    }

    /**
     * Use this if you intend to override produce.
     */
    protected AbstractPage(ModelHtmlRenderer renderer) {
        this.handler  = null;
        this.renderer = notNull(renderer);
    }

    /**
     * Use this if you intend to override produce and get.
     */
    protected AbstractPage(final String regexp, final ModelHtmlRenderer renderer) {
        this.handler  = new AbstractRequestHandler(regexp){
            @Override
            public AbstractPage get(PageRequest request) {
                return AbstractPage.this.get(request);
            }
        };
        this.renderer = notNull(renderer);
    }

    /**
     * Use this if you intend to override produce and get.
     */
    protected AbstractPage(final String regexp) {
        this.handler  = new AbstractRequestHandler(regexp){
            @Override
            public AbstractPage get(PageRequest request) {
                return AbstractPage.this.get(request);
            }
        };
        this.renderer = null;
    }

    /**
     * Use this for producing instances that will be used as Models.
     * In other words, this constructor is usually used when overriding the
     * produce method.
     */
    protected AbstractPage() {
        this.handler  = null;
        this.renderer = null;
    }

    @Override
    public PageResponse produce(PageRequest request) {
        if (handler==null) {
            String message = "Produce must be overridden if no handler is specified";
            throw new IllegalStateException(message);
        }
        PageResponse response = handler.produce(request);
        check(response.model);
        return response;
    }

    /**
     * Override this method if you are implementing a subtype that uses the
     * regexp constructor.
     */
    public AbstractPage get(PageRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UIElement render(Model model, ClientInfo client) {
        check(model);
        if (renderer!=null) {
            return renderer.render(model, client);
        }
        HTMLTags tags = HTMLTags.of();
        ObjectBrowser browser = ObjectBrowser.of(model);
        return HtmlPage.guts(tags.html(tags.body(browser.toHTML())));
    }

    void check(Model model) {
        Class c = getClass();
        if (!c.isInstance(model)) {
            String message = model + " is not a " + c;
            throw new IllegalArgumentException(message);
        }
    }
}

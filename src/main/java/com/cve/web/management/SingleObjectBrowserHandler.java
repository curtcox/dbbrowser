package com.cve.web.management;

import com.cve.util.Check;
import com.cve.web.core.handlers.AbstractRequestHandler;
import com.cve.web.core.Model;
import com.cve.web.core.PageRequest;

/**
 * Handle requests for browsing a single given object.
 * This is generally used as the last handler in a series to provide a
 * fallback error page.
 * @author curt
 */
public final class SingleObjectBrowserHandler extends AbstractRequestHandler {

    /**
     * The object we can browse.
     */
    final Object target;

    private SingleObjectBrowserHandler(Object target) {
        super("");
        this.target = Check.notNull(target);
    }

    public static SingleObjectBrowserHandler of(Object target) {
        return new SingleObjectBrowserHandler(target);
    }

    /**
     * Return a model for the object.
     */
    @Override
    public Model get(PageRequest request) {
        return ObjectModel.of(target);
    }

}

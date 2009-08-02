package com.cve.web;

import com.cve.util.Check;

/**
 * Wraps a Throwable to produce a model.
 */
final class ThrowableModel implements Model {

    private final Throwable t;

    ThrowableModel(Throwable t) {
        this.t = Check.notNull(t);
    }

    Throwable getThrowable() { return t; }

}

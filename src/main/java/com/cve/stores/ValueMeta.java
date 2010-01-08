package com.cve.stores;

import javax.annotation.concurrent.Immutable;

/**
 * Information about a curent value.
 * @author curt
 */
@Immutable
public final class ValueMeta {

    final Throwable problem;

    final long timeStamp = System.currentTimeMillis();

    /**
     * Use the factory
     */
    private ValueMeta(Throwable problem) {
        this.problem = problem;
    }

    static ValueMeta of() {
        return new ValueMeta(null);
    }

    static ValueMeta of(Throwable t) {
        return new ValueMeta(t);
    }

}

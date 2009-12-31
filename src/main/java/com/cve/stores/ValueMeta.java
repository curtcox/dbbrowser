package com.cve.stores;

import javax.annotation.concurrent.Immutable;

/**
 * Information about a curent value.
 * @author curt
 */
@Immutable
public final class ValueMeta {

    /**
     * Use the factory
     */
    private ValueMeta() {}

    static ValueMeta of() {
        return new ValueMeta();
    }

}

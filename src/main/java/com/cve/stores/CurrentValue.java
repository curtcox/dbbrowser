package com.cve.stores;

import javax.annotation.concurrent.Immutable;
/**
 * The current value of something -- as far as we know.
 * This is the flip side of java.util.concurrent.Future.
 * @author curt
 */
@Immutable
public final class CurrentValue<T> {

    /**
     * The value.
     */
    public final T value;

    /**
     * Information about the value.
     */
    public final ValueMeta meta;

    private CurrentValue(T value, ValueMeta meta) {
        this.value = value;
        this.meta = meta;
    }

    public static <T> CurrentValue<T> of(T t) {
        ValueMeta meta = ValueMeta.of();
        return new CurrentValue(t,meta);
    }

}

package com.cve.stores;

import javax.annotation.concurrent.Immutable;
/**
 * The current value of something -- as far as we know.
 * This is the flip side of java.util.concurrent.Future.
 * Generally speaking, things that return a CurrentValue should do so
 * immediately and not include an exception in their method signature.
 * Exceptions encountered during past invocations can be obtained via the
 * ValueMeta.
 * <p>
 * Returning a value from a database connection is the example driving this
 * class.
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

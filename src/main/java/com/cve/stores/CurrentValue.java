package com.cve.stores;

import com.cve.log.Log;
import com.cve.util.Check;
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

    /**
     * Where we log to.
     */
    private static final Log LOG = Log.of(CurrentValue.class);

    private CurrentValue(T value, ValueMeta meta) {
        this.value = Check.notNull(value);
        this.meta  = Check.notNull(meta);
    }

    public static <T> CurrentValue<T> of(T value) {
        ValueMeta meta = ValueMeta.of();
        return new CurrentValue(value,meta);
    }

    public static <T> CurrentValue<T> of(T value, Throwable t) {
        ValueMeta meta = ValueMeta.of(t);
        t.printStackTrace();
        LOG.warn(t);
        return new CurrentValue(value,meta);
    }

    @Override
    public String toString() {
        return "<CurrentValue>" +
                    " value=" + value +
                    " meta=" + meta +
               "</CurrentValue>";
    }
}

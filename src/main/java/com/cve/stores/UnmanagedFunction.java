package com.cve.stores;

import static com.cve.util.Check.notNull;

/**
 * Not a ManagedFunction, but useful for testing.
 * @author curt
 */
public final class UnmanagedFunction implements ManagedFunction {

    /**
     * The function we don't manage
     */
    final UnpredictableFunction f;

    /**
     * The function maps from this class.
     */
    final Class domain;

    /**
     * The function maps to this class.
     */
    final Class range;
    
    /**
     * The value to use when we don't have a better one.
     */
    final Object nullValue;

    private UnmanagedFunction(UnpredictableFunction f, Class domain, Class range, Object nullValue) {
        this.f = notNull(f);
        this.domain = notNull(domain);
        this.range = notNull(range);
        this.nullValue = nullValue;
    }

    public static ManagedFunction of(UnpredictableFunction f, Class domain, Class range, Object nullValue) {
        return new UnmanagedFunction(f,domain,range,nullValue);
    }

    @Override
    public CurrentValue apply(final Object from) {
        domain.cast(from);
        try {
            Object value = f.apply(from);
            range.cast(value);
            return CurrentValue.of(value);
        } catch (Exception e) {
            e.printStackTrace();
            return CurrentValue.of(nullValue, e);
        }
    }
}

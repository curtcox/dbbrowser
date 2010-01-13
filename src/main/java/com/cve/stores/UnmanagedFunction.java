package com.cve.stores;

/**
 * Not a ManagedFunction, but useful for testing.
 * @author curt
 */
public class UnmanagedFunction implements ManagedFunction {

    /**
     * The function we don't manage
     */
    final UnpredictableFunction f;

    /**
     * The value to use when we don't have a better one.
     */
    final Object nullValue;

    UnmanagedFunction(UnpredictableFunction f, Object nullValue) {
        this.f = f;
        this.nullValue = nullValue;
    }

    @Override
    public CurrentValue apply(Object from) {
        try {
            return CurrentValue.of(f.apply(from));
        } catch (Exception e) {
            return CurrentValue.of(nullValue, e);
        }
    }
}

package com.cve.stores;

/**
 *
 * @author curt
 */
final class SimpleManagedFunction implements ManagedFunction {

    private final UnpredictableFunction f;

    private SimpleManagedFunction(UnpredictableFunction f) {
        this.f = f;
    }

    private static SimpleManagedFunction of(UnpredictableFunction f) {
        return new SimpleManagedFunction(f);
    }

    @Override
    public CurrentValue apply(Object from) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

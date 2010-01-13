package com.cve.stores;

/**
 *
 * @author Curt
 */
public final class LocalManagedFunctionFactory implements ManagedFunction.Factory {

    private final Store.Factory stores;

    private LocalManagedFunctionFactory(Store.Factory stores) {
        this.stores = stores;
    }

    public static ManagedFunction.Factory of(Store.Factory stores) {
        return new LocalManagedFunctionFactory(stores);
    }

    @Override
    public ManagedFunction of(UnpredictableFunction f, Object nullValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

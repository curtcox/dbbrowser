package com.cve.stores;

/**
 * Not a real ManagedFunction.Factory, but useful for testing.
 * @author Curt
 */
public final class UnmanagedFunctionFactory implements ManagedFunction.Factory {

    private UnmanagedFunctionFactory() {}

    public static ManagedFunction.Factory of() {
        return new UnmanagedFunctionFactory();
    }

    @Override
    public ManagedFunction of(UnpredictableFunction f, Object nullValue) {
        return new UnmanagedFunction(f,nullValue);
    }

}

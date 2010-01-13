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

    private static class UnmanagedFunction implements ManagedFunction {

        final UnpredictableFunction f;

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
                return CurrentValue.of(nullValue,e);
            }
        }
    }

}

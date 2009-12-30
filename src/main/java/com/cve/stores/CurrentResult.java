package com.cve.stores;

/**
 *
 * @author curt
 */
public final class CurrentResult<T> {

    public final T value;

    public final ResultMeta meta;

    private CurrentResult(T value, ResultMeta meta) {
        this.value = value;
        this.meta = meta;
    }

    public static <T> CurrentResult<T> of(T t) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

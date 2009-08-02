package com.cve.db;

import javax.annotation.concurrent.Immutable;
/**
 * The typed value of a {@link Column} {@link Cell}, {@link Join}, or {@link Filter}.
 */
@Immutable

public final class Value {

    private final Object value;

    private static final Value NULL = new Value(null);

    public static Value of(Object value) {
        if (value==null) {
            return NULL;
        }
        return new Value(value);
    }

    private Value(Object value) {
        this.value = value;
    }

    public Object getValue() { return value;  }

    @Override
    public String toString() { return value.toString(); }

    @Override
    public int hashCode() {
        if (this==NULL) {
            return 0;
        }
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Value other = (Value) o;
        if (this==NULL) {
            return this==o;
        }
        return value.equals(other.value);
    }
}

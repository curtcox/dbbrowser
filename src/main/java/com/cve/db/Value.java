package com.cve.db;

import java.net.URLDecoder;
import java.net.URLEncoder;
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

    public static Value decode(String string) {
        string = URLDecoder.decode(string);
        string = string.replace("_", " ");
        string = string.replace("%2B", "+");
        return Value.of(string);
    }

    public String encode() {
        String  string = "" + value;
        string = string.replace(" ", "_");
        string = string.replace("+", "%2B");
        return URLEncoder.encode(string);
    }

    public Object getValue() { return value;  }

    @Override
    public String toString() { return "" + value; }

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

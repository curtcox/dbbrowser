package com.cve.model.db;

import com.cve.util.URLCodec;
import javax.annotation.concurrent.Immutable;
import org.h2.table.Column;
/**
 * The typed value of a {@link Column} {@link Cell}, {@link Join}, or {@link Filter}.
 */
@Immutable
public final class DBValue {

    /**
     * The value we hold.
     */
    public final Object value;

    /**
     * Use in place of a null value.
     */
    private static final DBValue NULL = new DBValue(null);

    public static DBValue of(Object value) {
        if (value==null) {
            return NULL;
        }
        return new DBValue(value);
    }

    private DBValue(Object value) {
        this.value = value;
    }

    public static DBValue decode(String string) {
        string = URLCodec.decode(string);
        return DBValue.of(string);
    }

    public String encode() {
        String  string = "" + value;
        return URLCodec.encode(string);
    }

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
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        DBValue other = (DBValue) o;
        if (this==NULL) {
            return this==o;
        }
        return value.equals(other.value);
    }
}

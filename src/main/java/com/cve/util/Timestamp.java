package com.cve.util;

import javax.annotation.concurrent.Immutable;

/**
 * Because java.util.Date is mutable.
 * @author curt
 */
@Immutable
public final class Timestamp implements Comparable<Timestamp> {

    final long value;

    private Timestamp(long value) {
        this.value = value;
    }

    public static Timestamp of(long millis) {
        return new Timestamp(millis);
    }

    public static Timestamp of() {
        return new Timestamp(System.currentTimeMillis());
    }

    @Override
    public int compareTo(Timestamp timeStamp) {
        if (value > timeStamp.value) {
            return 1;
        }
        if (value < timeStamp.value) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }
}

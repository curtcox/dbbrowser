package com.cve.util;

import javax.annotation.concurrent.Immutable;

/**
 * Because java.util.Date is mutable.
 * @author curt
 */
@Immutable
public final class Timestamp {

    final long value;

    private Timestamp(long value) {
        this.value = value;
    }

    static Timestamp of() {
        return new Timestamp(System.currentTimeMillis());
    }
}

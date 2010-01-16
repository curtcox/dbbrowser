package com.cve.io.db;

import com.cve.stores.UnpredictableFunction;

/**
 * A function that might throw a SQL exception.
 * If Function from Google Collection threw a SQLException, we would be
 * using it instead.
 * @author curt
 */
public abstract class SQLFunction<F,T> implements UnpredictableFunction<F,T> {

    public final Class range;
    public final Class domain;

    public SQLFunction(Class domain, Class range) {
        this.range = range;
        this.domain = domain;
    }
}

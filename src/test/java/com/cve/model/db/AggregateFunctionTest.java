package com.cve.model.db;

import com.cve.model.db.AggregateFunction;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author curt
 */
public class AggregateFunctionTest {

    @Test
    public void emptyParsesToIdentity() {
        assertEquals(AggregateFunction.IDENTITY,AggregateFunction.parse(""));
    }

    @Test
    public void countParsesToCount() {
        assertEquals(AggregateFunction.COUNT,AggregateFunction.parse("count"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void nonsenseParsesToException() {
        AggregateFunction.parse("nonsense");
    }

}

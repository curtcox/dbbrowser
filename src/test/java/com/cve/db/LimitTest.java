package com.cve.db;

import com.cve.db.Limit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class LimitTest {

    @Test
    public void equality() {
        assertEquals(Limit.limit(10),Limit.limit(10));
        assertEquals(Limit.limit(20),Limit.limit(20));
        assertEquals(Limit.limitOffset(10, 20),Limit.limitOffset(10,20));
        assertEquals(Limit.limitOffset(20, 20),Limit.limitOffset(20,20));
    }

    @Test
    public void inequality() {
        assertNotEquals(Limit.limit(10),Limit.limit(11));
        assertNotEquals(Limit.limit(20),Limit.limit(21));
        assertNotEquals(Limit.limitOffset(10, 20),Limit.limitOffset(10,21));
        assertNotEquals(Limit.limitOffset(20, 20),Limit.limitOffset(21,20));
    }

    private void assertNotEquals(Limit limit1, Limit limit2) {
        assertFalse(limit1.equals(limit2));
    }

    @Test
    public void next() {
        assertEquals(Limit.limit(1).next(1),Limit.limitOffset(1, 1));
        assertEquals(Limit.limit(1).next(2),Limit.limitOffset(1, 2));
        assertEquals(Limit.limitOffset(1,1).next(1),Limit.limitOffset(1, 2));
    }
}

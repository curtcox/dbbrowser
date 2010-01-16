package com.cve.model.db;

import com.cve.model.db.DBLimit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class LimitTest {

    @Test
    public void equality() {
        assertEquals(DBLimit.limit(10),DBLimit.limit(10));
        assertEquals(DBLimit.limit(20),DBLimit.limit(20));
        assertEquals(DBLimit.limitOffset(10, 20),DBLimit.limitOffset(10,20));
        assertEquals(DBLimit.limitOffset(20, 20),DBLimit.limitOffset(20,20));
    }

    @Test
    public void inequality() {
        assertNotEquals(DBLimit.limit(10),DBLimit.limit(11));
        assertNotEquals(DBLimit.limit(20),DBLimit.limit(21));
        assertNotEquals(DBLimit.limitOffset(10, 20),DBLimit.limitOffset(10,21));
        assertNotEquals(DBLimit.limitOffset(20, 20),DBLimit.limitOffset(21,20));
    }

    private void assertNotEquals(DBLimit limit1, DBLimit limit2) {
        assertFalse(limit1.equals(limit2));
    }

    @Test
    public void next() {
        assertEquals(DBLimit.limit(1).next(1),DBLimit.limitOffset(1, 1));
        assertEquals(DBLimit.limit(1).next(2),DBLimit.limitOffset(1, 2));
        assertEquals(DBLimit.limitOffset(1,1).next(1),DBLimit.limitOffset(1, 2));
    }
}

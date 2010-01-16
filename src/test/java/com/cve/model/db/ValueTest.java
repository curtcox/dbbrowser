package com.cve.model.db;

import com.cve.model.db.DBValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ValueTest {

    public ValueTest() {}


    @Test
    public void firstEqualsFirst() {
        assertEquals(DBValue.of("first"), DBValue.of("first"));
    }

    @Test
    public void oneNotEqualTwo() {
        assertFalse(DBValue.of(1).equals(DBValue.of(2)));
    }

    @Test
    public void oneEqualsOne() {
        assertEquals(DBValue.of(1),DBValue.of(1));
    }

}
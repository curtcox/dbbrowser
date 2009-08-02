package com.cve.db;

import com.cve.db.Value;
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
        assertEquals(Value.of("first"), Value.of("first"));
    }

    @Test
    public void oneNotEqualTwo() {
        assertFalse(Value.of(1).equals(Value.of(2)));
    }

    @Test
    public void oneEqualsOne() {
        assertEquals(Value.of(1),Value.of(1));
    }

}
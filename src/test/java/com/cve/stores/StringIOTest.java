package com.cve.stores;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class StringIOTest {

    static StringIO io = StringIO.of();

    @Test
    public void both() {
        both("one");
        both("two");
    }

    void both(String s) {
         assertEquals(s, io.parse(io.format(s)));
    }

}

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
    public void equality() {
        equality("one");
        equality("two");
    }

    @Test
    public void inequality() {
        inequality("one","two");
    }

    void equality(String s) {
         assertEquals(s, io.read(io.write(s)));
    }

    void inequality(String s1, String s2) {
         assertFalse(s1.equals(io.read(io.write(s2))));
         assertFalse(s2.equals(io.read(io.write(s1))));
    }

}

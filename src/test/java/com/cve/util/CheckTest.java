package com.cve.util;

import org.junit.Test;


/**
 *
 * @author curt
 */
public class CheckTest {

    public CheckTest() {}


    /**
     * Test of getSelect method, of class SimpleSelectResults.
     */
    @Test(expected=NullPointerException.class)
    public void nullThrows() {
        Check.notNull(null);
    }


}

package com.cve.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ReplaceTest {

    @Test
    public void nullThrows() {
        assertEquals("",Replace.bracketQuote(""));
        assertEquals("\"\"",Replace.bracketQuote("[]"));
        assertEquals("\"\"\"\"",Replace.bracketQuote("[[]]"));
        assertEquals("\"\"\"\"",Replace.bracketQuote("[][]"));
        assertEquals("\"1\"\"2\"",Replace.bracketQuote("[1][2]"));
    }

}

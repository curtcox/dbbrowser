package com.cve.db;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class RowTest {

    public RowTest() {}


    @Test
    public void firstEquality() {
        assertEquals(DBRow.FIRST, DBRow.FIRST);
    }

    @Test
    public void nextEquality() {
        assertEquals(DBRow.FIRST.next(), DBRow.FIRST.next());
    }

}
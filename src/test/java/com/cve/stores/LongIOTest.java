package com.cve.stores;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class LongIOTest {

    static LongIO io = LongIO.of();

    @Test
    public void both() {
        both(1L);
        both(2L);
    }

    void both(Long num) {
         assertEquals(num, io.read(io.write(num)));
    }

}

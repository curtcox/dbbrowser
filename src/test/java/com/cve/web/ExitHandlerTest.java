package com.cve.web;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ExitHandlerTest {

    private final ExitHandler   handler = ExitHandler.newInstance();

    @Test
    public void handlesExit() throws IOException {
        assertTrue(handler.handles("/exit"));
    }

}

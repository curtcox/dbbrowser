package com.cve.web.core.handlers;

import com.cve.web.core.handlers.ExitHandler;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ExitHandlerTest {

    private final ExitHandler   handler = ExitHandler.of();

    @Test
    public void handlesExit() throws IOException {
        assertTrue(handler.handles("/exit"));
    }

}

package com.cve.web.db;

import com.cve.web.*;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class DBBrowserHandlerTest {

    @Test
    public void handle() throws IOException, SQLException {
        RequestHandler handler = DBBrowserHandler.of(null,null,null,null);
        PageRequest   request = PageRequest.path("//server/");
        assertNotNull(handler.produce(request));
    }
}

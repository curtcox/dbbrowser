package com.cve.web;

import com.cve.log.Log;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Curt
 */
public class ResourceHandlerTest {

     Log log;

     @Test
     public void servesReouseHandler() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/resource/com/cve/web/ResourceHandler.java");
        RequestHandler handler = ResourceHandler.of(log);
        assertNotNull(handler.produce(request));
     }
}

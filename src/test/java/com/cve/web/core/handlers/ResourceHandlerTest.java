package com.cve.web.core.handlers;

import com.cve.web.core.RequestHandler;
import com.cve.web.core.PageRequest;
import com.cve.web.core.handlers.ResourceHandler;
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

     ;

     @Test
     public void servesReouseHandler() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/resource/com/cve/web/ResourceHandler.java");
        RequestHandler handler = ResourceHandler.of();
        assertNotNull(handler.produce(request));
     }
}

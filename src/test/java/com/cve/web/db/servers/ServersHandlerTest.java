package com.cve.web.db.servers;

import com.cve.web.*;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ServersHandlerTest {

    @Test
    public void handlesServersOnlyRequest() throws IOException, SQLException {
        ServersHandler handler = ServersHandler.of(null);
        PageRequest   request = PageRequest.path("/");
        assertNotNull(handler.produce(request));
    }

    @Test
    public void handlesServersOnlyRequestWithSearch() throws IOException, SQLException {
        ServersHandler handler = ServersHandler.of(null);
        PageRequest   request = PageRequest.path("/search/");
        assertNotNull(handler.produce(request));
    }

    @Test
    public void skipsNonServersOnlyRequest() throws IOException, SQLException {
        ServersHandler handler = ServersHandler.of(null);
        PageRequest   request = PageRequest.path("/+/server/");
        assertNull(handler.produce(request));
    }

}

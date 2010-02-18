package com.cve.web.db.servers;

import com.cve.web.core.PageRequest;
import com.cve.web.core.PageResponse;
import com.cve.log.Log;
import com.cve.web.*;
import com.cve.sample.db.DBSampleServerTestObjects;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class ServersHandlerTest {

    ;
    final ServersHandler handler = ServersHandler.of(DBSampleServerTestObjects.db,DBSampleServerTestObjects.serversStore);

    @Test
    public void producesServersOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/");
        PageResponse response = handler.produce(request);
        assertNotNull(response);
    }

    @Test
    public void getsServersOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/");
        ServersPage model = (ServersPage) handler.get(request);
        assertNotNull(model);
        assertEquals(1,model.servers.size());
        assertEquals(1,model.databases.size());
    }

    @Test
    public void handlesServersOnlyRequestWithSearch() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/search/");
        assertNotNull(handler.produce(request));
    }

    @Test
    public void skipsNonServersOnlyRequest() throws IOException, SQLException {
        PageRequest   request = PageRequest.path("/+/server/");
        assertNull(handler.produce(request));
    }

}

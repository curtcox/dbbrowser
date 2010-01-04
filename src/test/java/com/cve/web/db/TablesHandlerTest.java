package com.cve.web.db;

import com.cve.web.*;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author curt
 */
public class TablesHandlerTest {

    @Test
    public void handlesTablesOnlyRequest() throws IOException, SQLException {
        TablesHandler handler = TablesHandler.of(null,null);
        PageRequest   request = PageRequest.path("//server/db/");
        assertNotNull(handler.produce(request));
    }

    @Test
    public void isTablesOnlyRequest() {
        assertFalse(TablesHandler.isTablesOnlyRequest("//server/"));
        assertFalse(TablesHandler.isTablesOnlyRequest("//server:8080/"));
        assertTrue(TablesHandler.isTablesOnlyRequest("//server/db/"));
        assertFalse(TablesHandler.isTablesOnlyRequest("//server/db/table/"));
    }

}
